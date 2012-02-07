package org.scribe.builder;

import java.io.*;
import org.scribe.builder.api.*;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.oauth.*;
import org.scribe.utils.*;

/**
 * Implementation of the Builder pattern, with a fluent interface that creates a
 * {@link OAuthService}
 * 
 * @author Pablo Fernandez
 *
 */
public class ServiceBuilder
{
  private String apiKey;
  private String apiSecret;
  private String callback;
  private Api api;
  private String scope;
  private SignatureType signatureType;
  private OutputStream debugStream;
  
  /**
   * Default constructor
   */
  public ServiceBuilder()
  {
    this.callback = OAuthConstants.OUT_OF_BAND;
    this.signatureType = SignatureType.Header;
    this.debugStream = null;
  }
  
  /**
   * Configures the {@link Api}
   * 
   * @param apiClass the class of one of the existent {@link Api}s on org.scribe.api package
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder provider(Class<? extends Api> apiClass)
  {
    this.api = createApi(apiClass);
    return this;
  }

  private Api createApi(Class<? extends Api> apiClass)
  {
    Preconditions.checkNotNull(apiClass, "Api class cannot be null");
    Api api;
    try
    {
      api = apiClass.newInstance();  
    }
    catch(Exception e)
    {
      throw new OAuthException("Error while creating the Api object", e);
    }
    return api;
  }

  /**
   * Configures the {@link Api}
   *
   * Overloaded version. Let's you use an instance instead of a class.
   *
   * @param api instance of {@link Api}s
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder provider(Api api)
  {
	  Preconditions.checkNotNull(api, "Api cannot be null");
	  this.api = api;
	  return this;
  }

  /**
   * Adds an OAuth callback url
   * 
   * @param callback callback url. Must be a valid url or 'oob' for out of band OAuth
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder callback(String callback)
  {
    Preconditions.checkNotNull(callback, "Callback can't be null");
    this.callback = callback;
    return this;
  }
  
  /**
   * Configures the api key
   * 
   * @param apiKey The api key for your application
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder apiKey(String apiKey)
  {
    Preconditions.checkEmptyString(apiKey, "Invalid Api key");
    this.apiKey = apiKey;
    return this;
  }
  
  /**
   * Configures the api secret
   * 
   * @param apiSecret The api secret for your application
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder apiSecret(String apiSecret)
  {
    Preconditions.checkEmptyString(apiSecret, "Invalid Api secret");
    this.apiSecret = apiSecret;
    return this;
  }
  
  /**
   * Configures the OAuth scope. This is only necessary in some APIs (like Google's).
   * 
   * @param scope The OAuth scope
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder scope(String scope)
  {
    Preconditions.checkEmptyString(scope, "Invalid OAuth scope");
    this.scope = scope;
    return this;
  }

  /**
   * Configures the signature type, choose between header, querystring, etc. Defaults to Header
   *
   * @param scope The OAuth scope
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder signatureType(SignatureType type)
  {
    Preconditions.checkNotNull(type, "Signature type can't be null");
    this.signatureType = type;
    return this;
  }

  public ServiceBuilder debugStream(OutputStream stream)
  {
    Preconditions.checkNotNull(stream, "debug stream can't be null");
    this.debugStream = stream;
    return this;
  }

  public ServiceBuilder debug()
  {
    this.debugStream(System.out);
    return this;
  }
  
  /**
   * Returns the fully configured {@link OAuthService}
   * 
   * @return fully configured {@link OAuthService}
   */
  public OAuthService build()
  {
    Preconditions.checkNotNull(api, "You must specify a valid api through the provider() method");
    Preconditions.checkEmptyString(apiKey, "You must provide an api key");
    Preconditions.checkEmptyString(apiSecret, "You must provide an api secret");
    return api.createService(new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope, debugStream));
  }
}
