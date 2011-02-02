package org.scribe.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.*;

import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestTest
{

  private Request getRequest;
  private Request postRequest;
  private ConnectionStub connection;

  @Before
  public void setup() throws Exception
  {
    connection = new ConnectionStub();
    postRequest = new Request(Verb.POST, "http://example.com");
    postRequest.setConnection(connection);
    getRequest = new Request(Verb.GET, "http://example.com?qsparam=value&other=value+with+spaces");
    getRequest.setConnection(connection);
  }

  @Test
  public void shouldSetRequestVerb()
  {
    getRequest.send();
    assertEquals("GET", connection.getRequestMethod());
  }

  @Test
  public void shouldGetQueryStringParameters()
  {
    assertEquals(2, getRequest.getQueryStringParams().size());
    assertEquals(0, postRequest.getQueryStringParams().size());
    assertTrue(getRequest.getQueryStringParams().containsKey("qsparam"));
  }

  @Test
  public void shouldAddRequestHeaders()
  {
    getRequest.addHeader("Header", "1");
    getRequest.addHeader("Header2", "2");
    getRequest.send();
    assertThat(getRequest.getHeaders(), is(map(entry("Header", "1"), entry("Header2", "2"))));
    assertThat(connection.getHeaders(), is(map(entry("Header", "1"), entry("Header2", "2"),
            entry("Content-Type", "application/x-www-form-urlencoded"))));
  }

  public static <K,V> Map.Entry<K,V> entry(K k,V v) {
      return new AbstractMap.SimpleEntry<K, V>(k,v);
  }

  public static <K,V> Map<K,V> map(Map.Entry<K,V>... entries) {
      Map<K,V> result = new LinkedHashMap<K, V>();
      for (Map.Entry<K,V> entry: entries) {
          result.put(entry.getKey(), entry.getValue());
      }
      return result;
  }

  @Test
  public void shouldSetBodyParamsAndHeaders()
  {
    postRequest.addBodyParameter("param", "value");
    postRequest.addBodyParameter("param two", "value with spaces");
    postRequest.send();
    assertEquals("param%20two=value%20with%20spaces&param=value", new String(postRequest.getBodyContents()));
    assertTrue(connection.getHeaders().containsKey("Content-Length"));
  }

  @Test
  public void shouldSetPayloadAndHeaders()
  {
    postRequest.addPayload("PAYLOAD");
    postRequest.send();
    assertEquals("PAYLOAD", new String(postRequest.getBodyContents()));
    assertTrue(connection.getHeaders().containsKey("Content-Length"));
  }

  @Test
  public void shouldSetPayloadContentTypeAndHeaders()
  {
    postRequest.addPayload("PAYLOAD", "text/plain", Charset.forName("UTF-8"));
    postRequest.send();
    assertEquals("PAYLOAD", new String(postRequest.getBodyContents()));
    assertTrue(connection.getHeaders().containsKey("Content-Length"));
    assertTrue(connection.getHeaders().containsKey("Content-Type"));
    assertThat(connection.getHeaders().get("Content-Type").toLowerCase(), is("text/plain; charset=utf-8"));
  }

  @Test
  public void shouldAllowAddingQuerystringParametersAfterCreation()
  {
    Request request = new Request(Verb.GET, "http://example.com?one=val");
    request.addQuerystringParameter("two", "other val");
    request.addQuerystringParameter("more", "params");
    assertEquals(3, request.getQueryStringParams().size());
  }

}