package org.scribe.model;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

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
    assertTrue(getRequest.getQueryStringParams().get("other param").equals("value with spaces"));
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

  @Test
  public void shouldAllowAddingQuerystringParametersAfterCreation() throws MalformedURLException
  {
    Request request = new Request(Verb.GET, "http://example.com?one=val");
    request.addQuerystringParameter("two", "other val");
    request.addQuerystringParameter("more", "params");
    assertEquals(3, request.getQueryStringParams().size());
  }

  @Test
  public void sanatizeUrlTest() throws MalformedURLException
  {
    Request request = new Request(Verb.GET, "http://example.com?one=val");
    assertEquals("http://example.com", request.getSanitizedUrl());
    
    request = new Request(Verb.GET, "http://EXAMPLE.com:80/?one=val");
    assertEquals("http://example.com/", request.getSanitizedUrl());
    
    request = new Request(Verb.GET, "http://EXAMPLE.com:8080?one=val");
    assertEquals("http://example.com:8080", request.getSanitizedUrl());
    
    request = new Request(Verb.GET, "http://EXAMPLE.com:8080?one=val");
    assertEquals("http://example.com:8080", request.getSanitizedUrl());
    
    request = new Request(Verb.GET, "http://EXAMPLE.com:80/test/?one=val");
    assertEquals("http://example.com/test/", request.getSanitizedUrl());
    
    request = new Request(Verb.GET, "http://EXAMPLE.com:8080/test/?one=val#Hello");
    assertEquals("http://example.com:8080/test/", request.getSanitizedUrl());
    
    request = new Request(Verb.GET, "HTTP://EXAMPLE.com:8080/test/?one=val#Hello");
    assertEquals("http://example.com:8080/test/", request.getSanitizedUrl());
    
    //From RFC5849 example p.20
    request = new Request(Verb.GET, "http://example.com:80/r%20v/X?id=123");
    assertEquals("http://example.com/r%20v/X", request.getSanitizedUrl());
    
    //From RFC5849 example p.20
    request = new Request(Verb.GET, "https://www.example.com:8080/?q=1");
    assertEquals("https://www.example.com:8080/", request.getSanitizedUrl());
  }
  
}