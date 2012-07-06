package org.scribe.model;

import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.scribe.exceptions.OAuthException;

public interface Request
{
  String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
  String CONTENT_LENGTH = "Content-Length";
  String CONTENT_TYPE = "Content-Type";

  /**
   * Execute the request and return a {@link Response}
   * 
   * @return Http Response
   * @throws RuntimeException if the connection cannot be created.
   */
  Response send();

  void sendAsync(ResponseCallback callBack);

  /**
   * Returns the complete url (host + resource + encoded querystring parameters).
   * 
   * @return the complete url.
   */
  String getCompleteUrl();

  /**
   * Add an HTTP Header to the Request
   * 
   * @param key the header name
   * @param value the header value
   */
  void addHeader(String key, String value);

  /**
   * Add a body Parameter (for POST/ PUT Requests)
   * 
   * @param key the parameter name
   * @param value the parameter value
   */
  void addBodyParameter(String key, String value);

  /**
   * Add a QueryString parameter
   * 
   * @param key the parameter name
   * @param value the parameter value
   */
  void addQuerystringParameter(String key, String value);

  /**
   * Add body payload.
   * 
   * This method is used when the HTTP body is not a form-url-encoded string, but another thing. Like for example XML.
   * 
   * Note: The contents are not part of the OAuth signature
   * 
   * @param payload the body of the request
   */
  void addPayload(String payload);

  /**
   * Overloaded version for byte arrays
   * 
   * @param payload
   */
  void addPayload(byte[] payload);

  /**
   * Get a {@link ParameterList} with the query string parameters.
   * 
   * @return a {@link ParameterList} containing the query string parameters.
   * @throws OAuthException if the request URL is not valid.
   */
  ParameterList getQueryStringParams();

  /**
   * Obtains a {@link ParameterList} of the body parameters.
   * 
   * @return a {@link ParameterList}containing the body parameters.
   */
  ParameterList getBodyParams();

  /**
   * Obtains the URL of the HTTP Request.
   * 
   * @return the original URL of the HTTP Request
   */
  String getUrl();

  /**
   * Returns the URL without the port and the query string part.
   * 
   * @return the OAuth-sanitized URL
   */
  String getSanitizedUrl();

  /**
   * Returns the body of the request
   * 
   * @return form encoded string
   * @throws OAuthException if the charset chosen is not supported
   */
  String getBodyContents();

  /**
   * Returns the HTTP Verb
   * 
   * @return the verb
   */
  Verb getVerb();

  /**
   * Returns the connection headers as a {@link Map}
   * 
   * @return map of headers
   */
  Map<String, String> getHeaders();

  /**
   * Returns the connection charset. Defaults to {@link Charset} defaultCharset if not set
   * 
   * @return charset
   */
  String getCharset();

  /**
   * Sets the connect timeout for the underlying {@link HttpURLConnection}
   * 
   * @param duration duration of the timeout
   * 
   * @param unit unit of time (milliseconds, seconds, etc)
   */
  void setConnectTimeout(int duration, TimeUnit unit);

  /**
   * Sets the read timeout for the underlying {@link HttpURLConnection}
   * 
   * @param duration duration of the timeout
   * 
   * @param unit unit of time (milliseconds, seconds, etc)
   */
  void setReadTimeout(int duration, TimeUnit unit);

  /**
   * Set the charset of the body of the request
   * 
   * @param charsetName name of the charset of the request
   */
  void setCharset(String charsetName);

  /**
   * Sets whether the underlying Http Connection is persistent or not.
   * 
   * @see http ://download.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html
   * @param connectionKeepAlive
   */
  void setConnectionKeepAlive(boolean connectionKeepAlive);
}
