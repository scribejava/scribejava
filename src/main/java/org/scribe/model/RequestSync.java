package org.scribe.model;

import org.scribe.exceptions.*;

import java.io.IOException;
import java.net.*;

/**
 * Represents an HTTP Request object
 * 
 * @author Pablo Fernandez
 */
class RequestSync extends RequestBase
{
  private HttpURLConnection connection;

  /**
   * Creates a new Http Request
   * 
   * @param verb Http Verb (GET, POST, etc)
   * @param url url with optional querystring parameters.
   */
  public RequestSync(Verb verb, String url)
  {
    this.verb = verb;
    this.url = url;
  }

  /**
   * {@inheritDoc}
   */
  public Response send()
  {
    try
    {
      createConnection();
      return doSend();
    }
    catch (Exception e)
    {
      throw new OAuthConnectionException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void sendAsync(ResponseCallback callBack)
  {
    throw new OAuthException("Attempted to call sendAynch() method on an synchronous request, use send() instead");
  }

  private void createConnection() throws IOException
  {
    String completeUrl = getCompleteUrl();
    if (connection == null)
    {
      System.setProperty("http.keepAlive", connectionKeepAlive ? "true" : "false");
      connection = (HttpURLConnection) new URL(completeUrl).openConnection();
    }
  }

  Response doSend() throws IOException
  {
    connection.setRequestMethod(this.verb.name());
    if (connectTimeout != null)
    {
      connection.setConnectTimeout(connectTimeout.intValue());
    }
    if (readTimeout != null)
    {
      connection.setReadTimeout(readTimeout.intValue());
    }
    addHeaders(connection);
    if (verb.equals(Verb.PUT) || verb.equals(Verb.POST))
    {
      addBody(connection, getByteBodyContents());
    }
    return new Response(connection);
  }

  void addHeaders(HttpURLConnection conn)
  {
    for (String key : headers.keySet())
      conn.setRequestProperty(key, headers.get(key));
  }

  void addBody(HttpURLConnection conn, byte[] content) throws IOException
  {
    conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));

    // Set default content type if none is set.
    if (conn.getRequestProperty(CONTENT_TYPE) == null)
    {
      conn.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
    }
    conn.setDoOutput(true);
    conn.getOutputStream().write(content);
  }

  /**
   * We need this in order to stub the connection object for test cases
   */
  void setConnection(HttpURLConnection connection)
  {
    this.connection = connection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return String.format("@Request(%s %s)", getVerb(), getUrl());
  }
}
