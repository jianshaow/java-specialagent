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

import static net.bytebuddy.matcher.ElementMatchers.*;

import io.opentracing.contrib.specialagent.AgentRule;
import io.opentracing.contrib.specialagent.EarlyReturnException;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;
import net.bytebuddy.utility.JavaModule;

public class FilterAgentRule extends AgentRule {
  @Override
  public AgentBuilder buildAgentChainedGlobal1(final AgentBuilder builder) {
    return builder
      .type(hasSuperType(named("javax.servlet.http.HttpServlet")))
      .transform(new Transformer() {
        @Override
        public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module) {
          return builder
            .visit(advice(typeDescription).to(ServletInitAdvice.class).on(named("init").and(takesArguments(1)).and(takesArgument(0, named("javax.servlet.ServletConfig")))))
            .visit(advice(typeDescription).to(ServletServiceAdvice.class).on(named("service").and(takesArguments(2)).and(takesArgument(0, named("javax.servlet.http.HttpServletRequest")).and(takesArgument(1, named("javax.servlet.http.HttpServletResponse"))))));
        }})
      .type(not(isInterface()).and(hasSuperType(named("javax.servlet.http.HttpServletResponse"))))
      .transform(new Transformer() {
        @Override
        public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module) {
          return builder
            .visit(advice(typeDescription).to(StatusAdvice.class).on(named("setStatus").and(takesArguments(1)).and(takesArguments(int.class))))
            .visit(advice(typeDescription).to(StatusAdvice.class).on(named("setStatus").and(takesArguments(2)).and(takesArguments(int.class, String.class))))
            .visit(advice(typeDescription).to(StatusAdvice.class).on(named("sendError").and(takesArguments(2)).and(takesArguments(int.class, String.class))))
            .visit(advice(typeDescription).to(StatusAdvice.class).on(named("sendError").and(takesArguments(1)).and(takesArguments(int.class))))
            .visit(advice(typeDescription).to(ResetAdvice.class).on(named("reset")))
            .visit(advice(typeDescription).to(SendRedirectAdvice.class).on(named("sendRedirect").and(takesArguments(1)).and(takesArgument(0, String.class))));
        }})
      .type(not(isInterface()).and(hasSuperType(named("javax.servlet.Filter")).and(not(named("io.opentracing.contrib.web.servlet.filter.TracingFilter")))))
      .transform(new Transformer() {
        @Override
        public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module) {
          return builder
            .visit(advice(typeDescription).to(FilterInitAdvice.class).on(named("init").and(takesArguments(1)).and(takesArgument(0, named("javax.servlet.FilterConfig")))))
            .visit(advice(typeDescription).to(DoFilterEnter.class).on(named("doFilter").and(takesArguments(3)).and(takesArgument(0, named("javax.servlet.ServletRequest")).and(takesArgument(1, named("javax.servlet.ServletResponse")).and(takesArgument(2, named("javax.servlet.FilterChain")))))));
        }});
  }

  @Override
  public AgentBuilder buildAgentChainedGlobal2(final AgentBuilder builder) {
    return builder
      .type(not(isInterface()).and(hasSuperType(named("javax.servlet.Filter")).and(not(named("io.opentracing.contrib.web.servlet.filter.TracingFilter")))))
      .transform(new Transformer() {
        @Override
        public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module) {
          return builder.visit(advice(typeDescription).to(DoFilterExit.class).on(named("doFilter").and(takesArguments(3)).and(takesArgument(0, named("javax.servlet.ServletRequest")).and(takesArgument(1, named("javax.servlet.ServletResponse")).and(takesArgument(2, named("javax.servlet.FilterChain")))))));
        }});
  }

  public static class ServletInitAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @ClassName String className, final @Advice.Origin String origin, final @Advice.This Object thiz, final @Advice.Argument(value = 0) Object servletConfig) {
      if (isAllowed(className, origin))
        ServletAgentIntercept.init(thiz, servletConfig);
    }
  }

  public static class ServletServiceAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @ClassName String className, final @Advice.Origin String origin, final @Advice.This Object thiz, final @Advice.Argument(value = 0) Object request, final @Advice.Argument(value = 1) Object response) {
      if (isAllowed(className, origin))
        ServletAgentIntercept.serviceEnter(thiz, request, response);
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(final @ClassName String className, final @Advice.Origin String origin, final @Advice.Argument(value = 0) Object request, final @Advice.Argument(value = 1) Object response, final @Advice.Thrown Throwable thrown) {
      if (isAllowed(className, origin))
        ServletAgentIntercept.serviceExit(request, response, thrown);
    }
  }

  public static class StatusAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @ClassName String className, final @Advice.Origin String origin, final @Advice.This Object thiz, final @Advice.Argument(value = 0) int status) {
      if (isAllowed(className, origin))
        FilterAgentIntercept.setStatusCode(thiz, status);
    }
  }

  public static class ResetAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @ClassName String className, final @Advice.Origin String origin, final @Advice.This Object thiz) {
      if (isAllowed(className, origin))
        FilterAgentIntercept.setStatusCode(thiz, 200);
    }
  }

  public static class SendRedirectAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @ClassName String className, final @Advice.Origin String origin, final @Advice.This Object thiz) {
      if (isAllowed(className, origin))
        FilterAgentIntercept.setStatusCode(thiz, 302);
    }
  }

  public static class FilterInitAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @ClassName String className, final @Advice.Origin String origin, final @Advice.This Object thiz, final @Advice.Argument(value = 0) Object filterConfig) {
      if (isAllowed(className, origin))
        FilterAgentIntercept.init(thiz, filterConfig);
    }
  }

  public static class DoFilterEnter {
    @Advice.OnMethodEnter
    public static void enter(final @ClassName String className, final @Advice.Origin String origin, final @Advice.This Object thiz, final @Advice.Argument(value = 0) Object request, final @Advice.Argument(value = 1) Object response, final @Advice.Argument(value = 2) Object chain) {
      if (!ServletContextAgentRule.filterAdded && isAllowed(className, origin))
        FilterAgentIntercept.doFilter(thiz, request, response, chain);
    }
  }

  public static class DoFilterExit {
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Thrown(readOnly = false, typing = Typing.DYNAMIC) Throwable thrown) {
      if (thrown instanceof EarlyReturnException)
        thrown = null;
    }
  }
}