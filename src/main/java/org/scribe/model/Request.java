package org.scribe.model;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.scribe.exceptions.*;

/**
 * Represents an HTTP Request object
 * 
 * @author Pablo Fernandez
 */
public class Request
{
  public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

  protected String url;
  protected Verb verb;
  protected ParameterList querystringParams;
  protected ParameterList bodyParams;
  protected Map<String, String> headers;
  protected String payload = null;
  protected String charset;
  protected byte[] bytePayload = null;
  protected boolean connectionKeepAlive = false;
  protected Long connectTimeout = null;
  protected Long readTimeout = null;

  protected static HttpStrategy httpStrategy = new URLConnectionStrategy();

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
    this.querystringParams = new ParameterList();
    this.bodyParams = new ParameterList();
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
      return httpStrategy.send(this);
  }

  /**
   * Returns the complete url (host + resource + encoded querystring parameters).
   *
   * @return the complete url.
   */
  public String getCompleteUrl()
  {
    return querystringParams.appendTo(url);
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
    this.bodyParams.add(key, value);
  }

  /**
   * Add a QueryString parameter
   *
   * @param key the parameter name
   * @param value the parameter value
   */
  public void addQuerystringParameter(String key, String value)
  {
    this.querystringParams.add(key, value);
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
    this.payload = payload;
  }

  /**
   * Overloaded version for byte arrays
   *
   * @param payload
   */
  public void addPayload(byte[] payload)
  {
    this.bytePayload = payload;
  }

  /**
   * Get a {@link ParameterList} with the query string parameters.
   * 
   * @return a {@link ParameterList} containing the query string parameters.
   * @throws OAuthException if the request URL is not valid.
   */
  public ParameterList getQueryStringParams()
  {
    try
    {
      ParameterList result = new ParameterList();
      String queryString = new URL(url).getQuery();
      result.addQuerystring(queryString);
      result.addAll(querystringParams);
      return result;
    }
    catch (MalformedURLException mue)
    {
      throw new OAuthException("Malformed URL", mue);
    }
  }

  /**
   * Obtains a {@link ParameterList} of the body parameters.
   * 
   * @return a {@link ParameterList}containing the body parameters.
   */
  public ParameterList getBodyParams()
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
   * @throws OAuthException if the charset chosen is not supported
   */
  public String getBodyContents()
  {
    try
    {
      return new String(getByteBodyContents(),getCharset());
    }
    catch(UnsupportedEncodingException uee)
    {
      throw new OAuthException("Unsupported Charset: "+charset, uee);
    }
  }

  byte[] getByteBodyContents()
  {
    if (bytePayload != null) return bytePayload;
    String body = (payload != null) ? payload : bodyParams.asFormUrlEncodedString();
    try
    {
      return body.getBytes(getCharset());
    }
    catch(UnsupportedEncodingException uee)
    {
      throw new OAuthException("Unsupported Charset: "+getCharset(), uee);
    }
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
   * Returns the connection charset. Defaults to {@link Charset} defaultCharset if not set
   *
   * @return charset
   */
  public String getCharset()
  {
    return charset == null ? Charset.defaultCharset().name() : charset;
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
    this.connectTimeout = unit.toMillis(duration);
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
    this.readTimeout = unit.toMillis(duration);
  }

  /**
   * Set the charset of the body of the request
   *
   * @param charsetName name of the charset of the request
   */
  public void setCharset(String charsetName)
  {
    this.charset = charsetName;
  }

  /**
   * Sets whether the underlying Http Connection is persistent or not.
   *
   * @see http://download.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html
   * @param connectionKeepAlive
   */
  public void setConnectionKeepAlive(boolean connectionKeepAlive)
  {
    this.connectionKeepAlive = connectionKeepAlive;
  }

  @Override
  public String toString()
  {
    return String.format("@Request(%s %s)", getVerb(), getUrl());
  }

  public Long getConnectTimeout() {
      return connectTimeout;
  }

  public Long getReadTimeout() {
      return readTimeout;
  }

  public boolean isConnectionKeepAlive() {
      return connectionKeepAlive;
  }

  public static HttpStrategy getHttpStrategy() {
      return httpStrategy;
  }

  public static void setHttpStrategy(HttpStrategy httpStrategy) {
      Request.httpStrategy = httpStrategy;
  }

}
