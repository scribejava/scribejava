package org.scribe.model;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

public class ResponseTest
{

  private Response response;
  private ConnectionStub connection;

  @Before
  public void setup() throws Exception
  {
    connection = new ConnectionStub();
    connection.addResponseHeader("one", "one");
    connection.addResponseHeader("two", "two");
    response = new Response(connection);
  }

  @Test
  public void shouldPopulateResponseHeaders()
  {
    assertEquals(2, response.getHeaders().size());
    assertEquals("one", response.getHeader("one"));
  }

  @Test
  public void shouldParseBodyContents()
  {
    assertEquals("contents", response.getBody());
    assertEquals(1, connection.getTimesCalledInpuStream());
  }

  @Test
  public void shouldParseBodyContentsOnlyOnce()
  {
    assertEquals("contents", response.getBody());
    assertEquals("contents", response.getBody());
    assertEquals("contents", response.getBody());
    assertEquals(1, connection.getTimesCalledInpuStream());
  }

  @Test
  public void shouldHandleAConnectionWithErrors() throws Exception
  {
    Response errResponse = new Response(new FaultyConnection());
    assertEquals(400, errResponse.getCode());
    assertEquals("errors", errResponse.getBody());
  }

  private static class FaultyConnection extends ConnectionStub
  {

    public FaultyConnection() throws Exception
    {
      super();
    }

    @Override
    public InputStream getErrorStream()
    {
      return new ByteArrayInputStream("errors".getBytes());
    }

    @Override
    public int getResponseCode() throws IOException
    {
      return 400;
    }
  }
}
