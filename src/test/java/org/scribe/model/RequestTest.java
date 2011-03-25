package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.*;

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
    postRequest.addBodyParameter("param", "value");
    postRequest.addBodyParameter("param with spaces", "value with spaces");
    postRequest.setConnection(connection);
    getRequest = new Request(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces");
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
    assertTrue(getRequest.getQueryStringParams().get("qsparam").equals("value"));
  }

  @Test
  public void shouldAddRequestHeaders()
  {
    getRequest.addHeader("Header", "1");
    getRequest.addHeader("Header2", "2");
    getRequest.send();
    assertEquals(2, getRequest.getHeaders().size());
    assertEquals(2, connection.getHeaders().size());
  }

  @Test
  public void shouldSetBodyParamsAndAddContentLength()
  {
    assertEquals("param=value&param+with+spaces=value+with+spaces", postRequest.getBodyContents());
    postRequest.send();
    assertTrue(connection.getHeaders().containsKey("Content-Length"));
  }

  @Test
  public void shouldSetPayloadAndHeaders()
  {
    postRequest.addPayload("PAYLOAD");
    postRequest.send();
    assertEquals("PAYLOAD", postRequest.getBodyContents());
    assertTrue(connection.getHeaders().containsKey("Content-Length"));
  }

  @Test
  public void shouldAllowAddingQuerystringParametersAfterCreation()
  {
    Request request = new Request(Verb.GET, "http://example.com?one=val");
    request.addQuerystringParameter("two", "other val");
    request.addQuerystringParameter("more", "params");
    assertEquals(3, request.getQueryStringParams().size());
  }

  @Test
  public void shouldHandleQueryStringSpaceEncodingProperly()
  {
    assertTrue(getRequest.getQueryStringParams().get("other param").equals("value with spaces"));
  }
}