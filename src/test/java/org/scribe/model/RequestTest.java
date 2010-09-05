package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.model.Request.*;

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
    assertEquals(2, getRequest.getHeaders().size());
    assertEquals(2, connection.getHeaders().size());
  }

  @Test
  public void shouldSetBodyParamsAndHeaders()
  {
    postRequest.addBodyParameter("param", "value");
    postRequest.addBodyParameter("param two", "value with spaces");
    postRequest.send();
    assertEquals("param+two=value+with+spaces&param=value", postRequest.getBodyContents());
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

}