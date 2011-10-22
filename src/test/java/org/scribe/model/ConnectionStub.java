package org.scribe.model;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionStub extends HttpURLConnection
{

  private Map<String, String> headers = new HashMap<String, String>();
  private Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();
  private int inputStreamCalled = 0;

  public ConnectionStub() throws Exception
  {
    super(new URL("http://example.com"));
  }

  @Override
  public void setRequestProperty(String key, String value)
  {
    headers.put(key, value);
  }

  @Override
  public String getRequestProperty(String s)
  {
    return headers.get(s);
  }

  public Map<String, String> getHeaders()
  {
    return headers;
  }

  @Override
  public int getResponseCode() throws IOException
  {
    return 200;
  }

  @Override
  public InputStream getInputStream() throws IOException
  {
    inputStreamCalled++;
    return new ByteArrayInputStream("contents".getBytes());
  }

  public int getTimesCalledInpuStream()
  {
    return inputStreamCalled;
  }

  @Override
  public OutputStream getOutputStream() throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write("contents".getBytes());
    return baos;
  }

  @Override
  public Map<String, List<String>> getHeaderFields()
  {
    return responseHeaders;
  }

  public void addResponseHeader(String key, String value)
  {
    responseHeaders.put(key, Arrays.asList(value));
  }

  public void connect() throws IOException
  {
  }

  public void disconnect()
  {
  }

  public boolean usingProxy()
  {
    return false;
  }

}
