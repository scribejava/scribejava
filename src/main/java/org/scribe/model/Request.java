package org.scribe.model;

import java.io.*;
import java.net.*;
import java.util.*;

import org.scribe.exceptions.*;
import org.scribe.utils.*;

/**
 * Represents an HTTP Request object
 * 
 * @author Pablo Fernandez
 */
class Request
{
  private static final String CONTENT_LENGTH = "Content-Length";

  private String url;
  private Verb verb;
  private Map<String, String> bodyParams;
  private Map<String, String> headers;
  private String payload = null;
  private Integer timeout = null;
  private HttpURLConnection connection;

  /**
   * Creates a new Http Request
   * 
   * @param verb Http Verb (GET, POST, etc)
   * @param url url with optional querystring parameters.
   */
  public Request(Verb verb, String url)
  {
    this.verb = verb;
    this.url = url;
    this.bodyParams = new HashMap<String, String>();
    this.headers = new HashMap<String, String>();
    try
    {
      connection = (HttpURLConnection) new URL(url).openConnection();
      if(timeout != null) connection.setConnectTimeout(timeout);
    } catch (IOException ioe)
    {
      throw new OAuthException("Could not open connection to: " + url, ioe);
    }
  }

  /**
   * Execute the request and return a {@link Response}
   * 
   * @return Http Response
   * @throws RuntimeException
   *           if the connection cannot be created.
   */
  public Response send()
  {
    try
    {
      return doSend();
    } catch (IOException ioe)
    {
      throw new OAuthException("Problems while creating connection", ioe);
    }
  }

  Response doSend() throws IOException
  {
    connection.setRequestMethod(this.verb.name());
    addHeaders(connection);
    if (verb.equals(Verb.PUT) || verb.equals(Verb.POST))
    {
      addBody(connection, getBodyContents());
    }
    return new Response(connection);
  }

  void addHeaders(HttpURLConnection conn)
  {
    for (String key : headers.keySet())
      conn.setRequestProperty(key, headers.get(key));
  }

  void addBody(HttpURLConnection conn, String content) throws IOException
  {
    conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.getBytes().length));
    conn.setDoOutput(true);
    conn.getOutputStream().write(content.getBytes());
  }

  /**
   * Add an HTTP Header to the Request
   * 
   * @param name
   * @param value
   */
  public void addHeader(String key, String value)
  {
    this.headers.put(key, value);
  }

  /**
   * Add a body Parameter (for POST/ PUT Requests)
   * 
   * @param name
   * @param value
   */
  public void addBodyParameter(String key, String value)
  {
    this.bodyParams.put(key, value);
  }

  /**
   * Add body payload.
   * 
   * This method is used when the HTTP body is not a form-url-encoded string,
   * but another thing. Like for example XML.
   * 
   * Note: The contents are not part of the OAuth signature
   * 
   * @param payload
   */
  public void addPayload(String payload)
  {
    this.payload = payload;
  }

  /**
   * Get a {@link Map} of the query string parameters.
   * 
   * @return a map containing the query string parameters
   */
  public Map<String, String> getQueryStringParams()
  {
    try
    {
      Map<String, String> params = new HashMap<String, String>();
      String query = new URL(url).getQuery();
      if (query != null)
      {
        for (String param : query.split("&"))
        {
          String pair[] = param.split("=");
          params.put(pair[0], pair[1]);
        }
      }
      return params;
    } catch (MalformedURLException mue)
    {
      throw new OAuthException("Malformed URL", mue);
    }
  }

  /**
   * Obtains a {@link Map} of the body parameters.
   * 
   * @return a map containing the body parameters.
   */
  public Map<String, String> getBodyParams()
  {
    return bodyParams;
  }

  /**
   * Obtains the URL of the HTTP Request.
   * 
   * @return the original URL of the HTTP Request
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * Returns the URL without the port and the query string part.
   * 
   * @return the OAuth-sanitized URL
   */
  public String getSanitizedUrl()
  {
    return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
  }

  /**
   * Returns the body of the request
   * 
   * @return form encoded string
   */
  public String getBodyContents()
  {
    return (payload != null) ? payload : URLUtils.formURLEncodeMap(bodyParams);
  }

  /**
   * Returns the HTTP Verb
   * 
   * @return the verb
   */
  public Verb getVerb()
  {
    return verb;
  }
  
  /**
   * Returns the connection headers as a {@link Map}
   * 
   * @return map of headers
   */
  public Map<String, String> getHeaders()
  {
    return headers;
  }

  /**
   * Sets the connection timeout in milliseconds for the underlying {@link HttpURLConnection}
   * 
   * @param timeout in milliseconds
   */
  public void setTimeout(int timeout)
  {
    this.timeout = timeout;
  }
  
  /*
   * We need this in order to stub the connection object for test cases
   */
  void setConnection(HttpURLConnection connection)
  {
    this.connection = connection;
  }

  @Override
  public String toString()
  {
    return String.format("@Request(%s %s)", getVerb(), getUrl());
  }
}
