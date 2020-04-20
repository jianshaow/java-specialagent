/* Copyright 2020 The OpenTracing Authors
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

package io.opentracing.contrib.specialagent.rule.cxf;

import static org.junit.Assert.*;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.opentracing.contrib.specialagent.AgentRunner;
import io.opentracing.contrib.specialagent.rule.cxf.interceptor.AbstractSpanTagInterceptor;
import io.opentracing.contrib.specialagent.rule.cxf.interceptor.ClientSpanTagInterceptor;
import io.opentracing.contrib.specialagent.rule.cxf.interceptor.ServerSpanTagInterceptor;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;

@RunWith(AgentRunner.class)
public class CxfTest {
  private static final String BASE_URI = "http://127.0.0.1:48080";

  @BeforeClass
  public static void beforeClass() {
    Thread.currentThread().setContextClassLoader(CxfTest.class.getClassLoader());
  }

  @Before
  public void before(final MockTracer tracer) {
    tracer.reset();
  }

  @Test
  public void testRs(final MockTracer tracer) {
    System.setProperty(Configuration.INTERCEPTORS_CLIENT_IN, ClientSpanTagInterceptor.class.getName());
    System.setProperty(Configuration.INTERCEPTORS_SERVER_OUT, ServerSpanTagInterceptor.class.getName());
    final String msg = "hello";

    // prepare server
    final JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
    serverFactory.setAddress(BASE_URI);
    serverFactory.setServiceBean(new EchoImpl());
    final Server server = serverFactory.create();

    // prepare client
    final JAXRSClientFactoryBean clientFactory = new JAXRSClientFactoryBean();
    clientFactory.setServiceClass(Echo.class);
    clientFactory.setAddress(BASE_URI);
    final Echo echo = clientFactory.create(Echo.class);

    final String response = echo.echo(msg);

    assertEquals(msg, response);
    assertEquals(2, tracer.finishedSpans().size());

    final WebClient client = WebClient.create(BASE_URI);
    final String result = client.post(msg, String.class);

    assertEquals(msg, result);
    assertEquals(4, tracer.finishedSpans().size());

    final List<MockSpan> spans = tracer.finishedSpans();
    for (final MockSpan span : spans) {
      assertEquals(AbstractSpanTagInterceptor.SPAN_TAG_VALUE, span.tags().get(AbstractSpanTagInterceptor.SPAN_TAG_KEY));
    }

    server.destroy();
    serverFactory.getBus().shutdown(true);
  }

  @Test
  public void testWs(final MockTracer tracer) {
    final String msg = "hello";

    // prepare server
    final JaxWsServerFactoryBean serverFactory = new JaxWsServerFactoryBean();
    serverFactory.setServiceClass(Echo.class);
    serverFactory.setAddress(BASE_URI);
    serverFactory.setServiceBean(new EchoImpl());
    final Server server = serverFactory.create();

    // prepare client
    final JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
    clientFactory.setServiceClass(Echo.class);
    clientFactory.setAddress(BASE_URI);
    final Echo echo = (Echo)clientFactory.create();

    final String response = echo.echo(msg);

    assertEquals(msg, response);
    assertEquals(2, tracer.finishedSpans().size());

    server.destroy();
    serverFactory.getBus().shutdown(true);
  }

  @Path("/")
  @WebService
  public static interface Echo {
    @POST
    String echo(String msg);
  }

  public static class EchoImpl implements Echo {
    @Override
    public String echo(final String msg) {
      return msg;
    }
  }
}