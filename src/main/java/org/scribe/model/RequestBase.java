package org.scribe.model;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.scribe.exceptions.OAuthException;

/**
 * Base class for HTTP Requests.
 *
 * @author Brett Wooldridge
 */
abstract class RequestBase implements Request
{
  protected String url;
  protected Verb verb;
  protected ParameterList querystringParams;
  protected ParameterList bodyParams;
  protected Map<String, String> headers;
  private String payload = null;
  private String charset;
  private byte[] bytePayload = null;
  protected boolean connectionKeepAlive = false;
  protected Long connectTimeout = null;
  protected Long readTimeout = null;

  // Instance initializer.  Always runs without subclasses needing to call super().
  {
    this.querystringParams = new ParameterList();
    this.bodyParams = new ParameterList();
    this.headers = new HashMap<String, String>();
  }

  /**
   * {@inheritDoc}
   */
  public String getCompleteUrl()
  {
    return querystringParams.appendTo(url);
  }

  /**
   * {@inheritDoc}
   */
  public void addHeader(String key, String value)
  {
    this.headers.put(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void addBodyParameter(String key, String value)
  {
    this.bodyParams.add(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void addQuerystringParameter(String key, String value)
  {
    this.querystringParams.add(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void addPayload(String payload)
  {
    this.payload = payload;
  }

  /**
   * {@inheritDoc}
   */
  public void addPayload(byte[] payload)
  {
    this.bytePayload = payload;
  }

  /**
   * {@inheritDoc}
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
   * {@inheritDoc}
   */
  public ParameterList getBodyParams()
  {
    return bodyParams;
  }

  /**
   * {@inheritDoc}
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * {@inheritDoc}
   */
  public String getSanitizedUrl()
  {
    return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
  }

  /**
   * {@inheritDoc}
   */
  public String getBodyContents()
  {
    try
    {
      return new String(getByteBodyContents(), getCharset());
    }
    catch (UnsupportedEncodingException uee)
    {
      throw new OAuthException("Unsupported Charset: " + charset, uee);
    }
  }

  protected byte[] getByteBodyContents()
  {
    if (bytePayload != null)
      return bytePayload;
    String body = (payload != null) ? payload : bodyParams.asFormUrlEncodedString();
    try
    {
      return body.getBytes(getCharset());
    }
    catch (UnsupportedEncodingException uee)
    {
      throw new OAuthException("Unsupported Charset: " + getCharset(), uee);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Verb getVerb()
  {
    return verb;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, String> getHeaders()
  {
    return headers;
  }

  /**
   * {@inheritDoc}
   */
  public String getCharset()
  {
    return charset == null ? Charset.defaultCharset().name() : charset;
  }

  /**
   * {@inheritDoc}
   */
  public void setConnectTimeout(int duration, TimeUnit unit)
  {
    this.connectTimeout = unit.toMillis(duration);
  }

  /**
   * {@inheritDoc}
   */
  public void setReadTimeout(int duration, TimeUnit unit)
  {
    this.readTimeout = unit.toMillis(duration);
  }

  /**
   * {@inheritDoc}
   */
  public void setCharset(String charsetName)
  {
    this.charset = charsetName;
  }

  /**
   * {@inheritDoc}
   */
  public void setConnectionKeepAlive(boolean connectionKeepAlive)
  {
    this.connectionKeepAlive = connectionKeepAlive;
  }
}
