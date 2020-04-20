/* Copyright 2019 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentracing.contrib.specialagent.rule.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import io.opentracing.contrib.specialagent.AgentRuleUtil;
import io.opentracing.contrib.specialagent.EarlyReturnException;
import io.opentracing.contrib.specialagent.Level;
import io.opentracing.contrib.specialagent.rule.servlet.ext.TracingProxyFilter;
import io.opentracing.contrib.web.servlet.filter.ClassUtil;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.opentracing.util.GlobalTracer;

public class FilterAgentIntercept extends ServletFilterAgentIntercept {
  public static final WeakHashMap<ServletResponse,Integer> servletResponseToStatus = new WeakHashMap<>();

  public static void init(final Object thiz, final Object filterConfig) {
    if (filterConfig != null)
      filterOrServletToServletContext.put(thiz, ((FilterConfig)filterConfig).getServletContext());
  }

  public static void doFilter(final Object thiz, final Object req, final Object res, final Object chain) {
    // `thiz` should never be `TracingFilter`, but issue #391 suggests otherwise
    if (thiz instanceof TracingFilter)
      return;

    final ServletRequest request = (ServletRequest)req;
    if (request.getAttribute(TracingFilter.SERVER_SPAN_CONTEXT) != null)
      return;

    try {
      final Filter filter = (Filter)thiz;
      final ServletContext[] context = new ServletContext[1];
      if (!ContextAgentIntercept.invoke(context, request, getMethod(request.getClass(), "getServletContext")) || context[0] == null)
        context[0] = filterOrServletToServletContext.get(filter);

      final TracingFilter tracingFilter = context[0] != null ? getFilter(context[0], true) : new TracingProxyFilter(GlobalTracer.get(), null);

      // If the tracingFilter instance is not a TracingProxyFilter, then it was
      // created with ServletContext#addFilter. Therefore, the intercept of the
      // Filter#doFilter method is not necessary.
      if (!(tracingFilter instanceof TracingProxyFilter))
        return;

      if (logger.isLoggable(Level.FINER))
        logger.finer(">> TracingFilter.doFilter(" + AgentRuleUtil.getSimpleNameId(request) + "," + AgentRuleUtil.getSimpleNameId(res) + "," + AgentRuleUtil.getSimpleNameId(context[0]) + ")");

      tracingFilter.doFilter(request, (ServletResponse)res, new FilterChain() {
        @Override
        public void doFilter(final ServletRequest request, final ServletResponse response) throws IOException, ServletException {
          filter.doFilter(request, response, (FilterChain)chain);
          if (logger.isLoggable(Level.FINER))
            logger.finer("<< TracingFilter.doFilter(" + AgentRuleUtil.getSimpleNameId(request) + "," + AgentRuleUtil.getSimpleNameId(response) + "," + AgentRuleUtil.getSimpleNameId(context[0]) + ")");
        }
      });
    }
    catch (final Throwable t) {
      logger.log(Level.WARNING, t.getMessage(), t);
      return;
    }

    throw new EarlyReturnException();
  }

  public static void setStatusCode(final Object response, final int status) {
    if (logger.isLoggable(Level.FINER))
      logger.finer("<> FilterAgentIntercept.setStatusCode(" + AgentRuleUtil.getSimpleNameId(response) + "," + status + ")");

    servletResponseToStatus.put((ServletResponse)response, status);
  }

  public static int getSatusCode(final ServletResponse response) {
    final Integer statusCode = servletResponseToStatus.remove(response);
    if (logger.isLoggable(Level.FINER))
      logger.finer("<> FilterAgentIntercept.getSatusCode(" + AgentRuleUtil.getSimpleNameId(response) + "): " + statusCode);

    if (statusCode != null)
      return statusCode;

    final Method getStatusMethod = ClassUtil.getMethod(response.getClass(), "getStatus");
    try {
      return getStatusMethod == null ? null : (Integer)getStatusMethod.invoke(response);
    }
    catch (final IllegalAccessException e) {
      logger.log(Level.WARNING, e.getMessage(), e);
    }
    catch (final InvocationTargetException e) {
      logger.log(Level.WARNING, e.getCause().getMessage(), e.getCause());
    }

    return 200;
  }
}