package com.github.scribejava.core.builder;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.SignatureType;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.core.utils.Preconditions;

import java.io.OutputStream;

/**
 * Implementation of the Builder pattern, with a fluent interface that creates a {@link OAuthService}
 */
public class ServiceBuilder {

    private String callback;
    private String apiKey;
    private String apiSecret;
    private String scope;
    private String state;
    private SignatureType signatureType;
    private OutputStream debugStream;
    private String responseType = "code";
    private String userAgent;

    //sync version only
    private Integer connectTimeout;
    private Integer readTimeout;

    //not-default httpclient only
    private HttpClient.Config httpClientConfig;
    private HttpClient httpClient;

    public ServiceBuilder() {
        callback = OAuthConstants.OUT_OF_BAND;
        signatureType = SignatureType.Header;
    }

    /**
     * Adds an OAuth callback url
     *
     * @param callback callback url. Must be a valid url or 'oob' for out of band OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder callback(String callback) {
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
    public ServiceBuilder apiKey(String apiKey) {
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
    public ServiceBuilder apiSecret(String apiSecret) {
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
    public ServiceBuilder scope(String scope) {
        Preconditions.checkEmptyString(scope, "Invalid OAuth scope");
        this.scope = scope;
        return this;
    }

    /**
     * Configures the anti forgery session state. This is available in some APIs (like Google's).
     *
     * @param state The OAuth state
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder state(String state) {
        Preconditions.checkEmptyString(state, "Invalid OAuth state");
        this.state = state;
        return this;
    }

    /**
     * Configures the signature type, choose between header, querystring, etc. Defaults to Header
     *
     * @param signatureType SignatureType
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder signatureType(SignatureType signatureType) {
        Preconditions.checkNotNull(signatureType, "Signature type can't be null");
        this.signatureType = signatureType;
        return this;
    }

    public ServiceBuilder debugStream(OutputStream debugStream) {
        Preconditions.checkNotNull(debugStream, "debug stream can't be null");
        this.debugStream = debugStream;
        return this;
    }

    public ServiceBuilder responseType(String responseType) {
        Preconditions.checkEmptyString(responseType, "Invalid OAuth responseType");
        this.responseType = responseType;
        return this;
    }

    public ServiceBuilder connectTimeout(Integer connectTimeout) {
        Preconditions.checkNotNull(connectTimeout, "Connection timeout can't be null");
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ServiceBuilder readTimeout(Integer readTimeout) {
        Preconditions.checkNotNull(readTimeout, "Read timeout can't be null");
        this.readTimeout = readTimeout;
        return this;
    }

    public ServiceBuilder httpClientConfig(HttpClient.Config httpClientConfig) {
        Preconditions.checkNotNull(httpClientConfig, "httpClientConfig can't be null");
        this.httpClientConfig = httpClientConfig;
        return this;
    }

    /**
     * takes precedence over httpClientConfig
     *
     * @param httpClient externally created HTTP client
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public ServiceBuilder userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public ServiceBuilder debug() {
        debugStream(System.out);
        return this;
    }

    public void checkPreconditions() {
        Preconditions.checkEmptyString(apiKey, "You must provide an api key");
    }

    private OAuthConfig createConfig() {
        checkPreconditions();
        return new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope, debugStream, state, responseType,
                userAgent, connectTimeout, readTimeout, httpClientConfig, httpClient);
    }

    /**
     * Returns the fully configured {@link S}
     *
     * @param <S> OAuthService implementation (OAuth1/OAuth2/any API specific)
     * @param api will build Service for this API
     * @return fully configured {@link S}
     */
    public <S extends OAuthService> S build(BaseApi<S> api) {
        return api.createService(createConfig());
    }
}
