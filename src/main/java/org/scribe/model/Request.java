package org.scribe.model;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private static final String CONTENT_TYPE = "Content-Type";

    private String url;
  private Verb verb;
  private Map<String, String> querystringParams;
  private Map<String, String> bodyParams;
  private Map<String, String> headers;
  private byte[] payload = null;
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
    this.querystringParams = new HashMap<String, String>();
    this.bodyParams = new HashMap<String, String>();
    this.headers = new HashMap<String, String>();
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
      createConnection();
      return doSend();
    } catch (IOException ioe)
    {
      throw new OAuthException("Problems while creating connection", ioe);
    }
  }

  private void createConnection() throws IOException
  {
    String effectiveUrl = URLUtils.appendParametersToQueryString(url, querystringParams);
    if (connection == null)
    {
      connection = (HttpURLConnection) new URL(effectiveUrl).openConnection();
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
    if (!headers.containsKey(CONTENT_TYPE)) {
        conn.setRequestProperty(CONTENT_TYPE, "application/x-www-form-urlencoded");
    }
  }

  void addBody(HttpURLConnection conn, byte[] content) throws IOException
  {
    conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));
    conn.setDoOutput(true);
    conn.getOutputStream().write(content);
  }

  /**
   * Add an HTTP Header to the Request
   * 
   * @param key the header name
   * @param value the header value
   */
  public void addHeader(String key, String value)
  {
    this.headers.put(key, value);
  }

  /**
   * Add a body Parameter (for POST/ PUT Requests)
   * 
   * @param key the parameter name
   * @param value the parameter value
   */
  public void addBodyParameter(String key, String value)
  {
    this.bodyParams.put(key, value);
  }

  /**
   * Add a QueryString parameter
   *
   * @param key the parameter name
   * @param value the parameter value
   */
  public void addQuerystringParameter(String key, String value)
  {
    this.querystringParams.put(key, value);
  }

  /**
   * Add body payload.
   * 
   * This method is used when the HTTP body is not a form-url-encoded string,
   * but another thing. Like for example XML.
   * 
   * Note: The contents are not part of the OAuth signature
   * 
   * @param payload the body of the request
   */
  public void addPayload(String payload)
  {
    this.payload = payload.getBytes();
  }

  /**
   * Add body payload.
   *
   * This method is used when the HTTP body is not a form-url-encoded string,
   * but another thing. Like for example XML.
   *
   * Note: The contents are not part of the OAuth signature
   *
   * @param payload the body of the request
   */
  public void addPayload(byte[] payload)
  {
    this.payload = payload;
  }

  /**
   * Add body payload and set the content type header.
   *
   * This method is used when the HTTP body is not a form-url-encoded string,
   * but another thing, like for example {@code text/xml}.
   *
   * @param payload The payload.
   * @param contentType The content type of the payload (if the content type starts with {@code text/} and does not
   *                    include the charset, it will be added automatically.
   * @param charset The charset to encode the payload as.
   */
  public void addPayload(String payload, String contentType, Charset charset) {
    payload.getClass(); // throw NPE if null
    contentType.getClass(); // throw NPE if null
    charset.getClass(); // throw NPE if null
    contentType = contentType.trim();
    if (contentType.startsWith("text/")) {
      if (!contentType.contains("charset=")) {
        contentType = contentType + "; charset=" + charset.name();
      }
    }
    addPayload(payload.getBytes(charset),contentType);
  }

  /**
   * Add body payload and set the content type header.
   *
   * This method is used when the HTTP body is not a form-url-encoded string,
   * but another thing, like for example {@code text/xml}.
   *
   * @param payload The payload (which will be encoded using the system default charset).
   * @param contentType The content type of the payload (if the content type starts with {@code text/} and does not
   *                    include the charset, it will be added automatically.
   */
  public void addPayload(String payload, String contentType) {
    addPayload(payload, contentType, Charset.defaultCharset());
  }

  /**
   * Add body payload and set the content type header.
   *
   * This method is used when the HTTP body is not a form-url-encoded string,
   * but another thing, like for example {@code text/xml}.
   *
   * @param payload The payload
   * @param contentType The content type of the payload.
   */
  public void addPayload(byte[] payload, String contentType) {
    payload.getClass(); // throw NPE if null
    contentType.getClass(); // throw NPE if null
    addHeader(CONTENT_TYPE, contentType);
    addPayload(payload);
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
      params.putAll(querystringParams);
      return params;
    }
    catch (MalformedURLException mue)
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
  public byte[] getBodyContents()
  {
    return (payload != null) ? payload : URLUtils.formURLEncodeMap(bodyParams).getBytes();
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
   * Sets the connect timeout for the underlying {@link HttpURLConnection}
   * 
   * @param duration duration of the timeout
   * 
   * @param unit unit of time (milliseconds, seconds, etc)
   */
  public void setConnectTimeout(int duration, TimeUnit unit)
  {
    this.connection.setConnectTimeout((int) unit.toMillis(duration));
  }

  /**
   * Sets the read timeout for the underlying {@link HttpURLConnection}
   * 
   * @param duration duration of the timeout
   * 
   * @param unit unit of time (milliseconds, seconds, etc)
   */
  public void setReadTimeout(int duration, TimeUnit unit)
  {
    this.connection.setReadTimeout((int) unit.toMillis(duration));
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
