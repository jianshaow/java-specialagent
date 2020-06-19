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

package io.opentracing.contrib.specialagent.rule.spring.scheduling;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.common.WrapperProxy;
import io.opentracing.contrib.specialagent.LocalSpanContext;
import io.opentracing.contrib.specialagent.OpenTracingApiUtil;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

public class SpringSchedulingAgentIntercept {
  static final String COMPONENT_NAME = "spring-scheduled";

  public static void enter(final Object thiz) {
    final ScheduledMethodRunnable runnable = (ScheduledMethodRunnable)thiz;
    final Tracer tracer = GlobalTracer.get();
    final Span span = tracer
      .buildSpan(runnable.getMethod().getName())
      .withTag(Tags.COMPONENT.getKey(), COMPONENT_NAME)
      .withTag("class", runnable.getClass().getSimpleName())
      .withTag("method", runnable.getMethod().getName())
      .start();

    final Scope scope = tracer.activateSpan(span);
    LocalSpanContext.set(COMPONENT_NAME, span, scope);
  }

  public static void exit(final Throwable thrown) {
    final LocalSpanContext context = LocalSpanContext.get(COMPONENT_NAME);
    if (context == null)
      return;

    if (thrown != null)
      OpenTracingApiUtil.setErrorTag(context.getSpan(), thrown);

    context.closeAndFinish();
  }

  public static Object invoke(final Object arg) {
    return WrapperProxy.wrap(arg, new TracingMethodInvocation((MethodInvocation)arg));
  }
}