package org.scribe.model;

import org.scribe.exceptions.OAuthException;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The representation of an OAuth HttpRequest.
 * 
 * Adds OAuth-related functionality to the {@link Request}  
 * 
 * @author Pablo Fernandez
 */
public class OAuthRequest implements Request
{
  private static final String OAUTH_PREFIX = "oauth_";
  private Map<String, String> oauthParameters;
  private Request request;
  private static Boolean asyncAvailable;
  private static Constructor<?> asyncConstructor;

  // Static initializer to cache the asynchronous request class constructor, if Apache library is available.
  static
  {
    if (isApacheAsyncAvailable())
    {
      try
      {
        ClassLoader classLoader = Request.class.getClassLoader();
        Class<?> asyncClass = classLoader.loadClass("org.scribe.model.RequestAsync");
        asyncConstructor = asyncClass.getConstructor(Verb.class, String.class);
      }
      catch (Exception e)
      {
        throw new OAuthException("Failure loading asynchronous class", e);
      }
    }
  }

  // Instance initializer.  Always runs without subclasses needing to call super().
  {
    this.oauthParameters = new HashMap<String, String>();
  }

  /**
   * Default constructor.
   * 
   * @param verb Http verb/method
   * @param url resource URL
   */
  public OAuthRequest(Verb verb, String url)
  {
    request = new RequestSync(verb, url);
  }

  /**
   * Constructor that allows specifying whether the authentication will occur
   * synchronously or asynchronously.  If asynchronicity is specified, the sendAsync()
   * method should be used instead of the send() method.
   *
   * @param verb Http verb/method
   * @param url resource URL
   * @param async whether this request is synchronous or asynchronous
   */
  public OAuthRequest(Verb verb, String url, boolean async)
  {
    if (isApacheAsyncAvailable())
    {
      if (async)
      {
        try
        {
          request = (Request) asyncConstructor.newInstance(verb, url);
        }
        catch (Exception e)
        {
          throw new OAuthException("Failure creating asynchronous request", e);
        }
      }
      else
      {
        request = new RequestSync(verb, url);
      }
    }
    else
    {
      throw new OAuthException("Attempted to create asynchronous request, but Apache HTTP Components library is not available");
    }
  }

  /**
   * Adds an OAuth parameter.
   * 
   * @param key name of the parameter
   * @param value value of the parameter
   * 
   * @throws IllegalArgumentException if the parameter is not an OAuth parameter
   */
  public void addOAuthParameter(String key, String value)
  {
    oauthParameters.put(checkKey(key), value);
  }

  private String checkKey(String key)
  {
    if (key.startsWith(OAUTH_PREFIX) || key.equals(OAuthConstants.SCOPE))
    {
      return key;
    } 
    else
    {
      throw new IllegalArgumentException(String.format("OAuth parameters must either be '%s' or start with '%s'", OAuthConstants.SCOPE, OAUTH_PREFIX));
    }
  }

  /**
   * Returns the {@link Map} containing the key-value pair of parameters.
   * 
   * @return parameters as map
   */
  public Map<String, String> getOauthParameters()
  {
    return oauthParameters;
  }

  /**
   * {@inheritDoc}
   */
  public Response send()
  {
    return request.send();
  }

  /**
   * {@inheritDoc}
   */
  public void sendAsync(ResponseCallback callBack)
  {
    request.sendAsync(callBack);
  }

  /**
   * {@inheritDoc}
   */
  public String getCompleteUrl()
  {
    return request.getCompleteUrl();
  }

  /**
   * {@inheritDoc}
   */
  public void addHeader(String key, String value)
  {
    request.addHeader(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void addBodyParameter(String key, String value)
  {
    request.addBodyParameter(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void addQuerystringParameter(String key, String value)
  {
    request.addQuerystringParameter(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void addPayload(String payload)
  {
    request.addPayload(payload);
  }

  /**
   * {@inheritDoc}
   */
  public void addPayload(byte[] payload)
  {
    request.addPayload(payload);
  }

  /**
   * {@inheritDoc}
   */
  public ParameterList getQueryStringParams()
  {
    return request.getQueryStringParams();
  }

  /**
   * {@inheritDoc}
   */
  public ParameterList getBodyParams()
  {
    return request.getBodyParams();
  }

  /**
   * {@inheritDoc}
   */
  public String getUrl()
  {
    return request.getUrl();
  }

  /**
   * {@inheritDoc}
   */
  public String getSanitizedUrl()
  {
    return request.getSanitizedUrl();
  }

  /**
   * {@inheritDoc}
   */
  public String getBodyContents()
  {
    return request.getBodyContents();
  }

  /**
   * {@inheritDoc}
   */
  public Verb getVerb()
  {
    return request.getVerb();
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, String> getHeaders()
  {
    return request.getHeaders();
  }

  /**
   * {@inheritDoc}
   */
  public String getCharset()
  {
    return request.getCharset();
  }

  /**
   * {@inheritDoc}
   */
  public void setConnectTimeout(int duration, TimeUnit unit)
  {
    request.setConnectTimeout(duration, unit);
  }

  /**
   * {@inheritDoc}
   */
  public void setReadTimeout(int duration, TimeUnit unit)
  {
    request.setReadTimeout(duration, unit);
  }

  /**
   * {@inheritDoc}
   */
  public void setCharset(String charsetName)
  {
    request.setCharset(charsetName);
  }

  /**
   * {@inheritDoc}
   */
  public void setConnectionKeepAlive(boolean connectionKeepAlive)
  {
    request.setConnectionKeepAlive(connectionKeepAlive);
  }

  /**
   * Checks whether the Apache HTTP Components asynchronous HTTP Client is available in the classpath. We do this because we want to avoid a hard runtime dependency.
   * 
   * @return true if the Apache asynchronous HTTP Client is available, false otherwise
   */
  private static boolean isApacheAsyncAvailable()
  {
    if (asyncAvailable != null)
    {
      return asyncAvailable;
    }

    try
    {
      ClassLoader classLoader = Request.class.getClassLoader();
      classLoader.loadClass("org.apache.http.nio.protocol.HttpAsyncRequester");
      asyncAvailable = true;
    }
    catch (ClassNotFoundException e)
    {
      try
      {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        classLoader.loadClass("org.apache.http.nio.protocol.HttpAsyncRequester");
        asyncAvailable = true;
      }
      catch (ClassNotFoundException e1)
      {
        asyncAvailable = false;
      }
    }

    return asyncAvailable;
  }

  @Override
  public String toString()
  {
    return String.format("@OAuthRequest(%s, %s)", request.getVerb(), request.getUrl());
  }
}
