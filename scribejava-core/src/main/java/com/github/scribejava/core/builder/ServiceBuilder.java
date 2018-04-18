package com.github.scribejava.core.builder;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
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
    private OutputStream debugStream;
    private String responseType = "code";
    private String userAgent;

    private HttpClientConfig httpClientConfig;
    private HttpClient httpClient;

    public ServiceBuilder(String apiKey) {
        apiKey(apiKey);
    }

    /**
     * Adds an OAuth callback url
     *
     * @param callback callback url. Must be a valid url or 'oob'
     * ({@link com.github.scribejava.core.model.OAuthConstants#OOB} for out of band OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder callback(String callback) {
        this.callback = callback;
        return this;
    }

    /**
     * Configures the api key
     *
     * @param apiKey The api key for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public final ServiceBuilder apiKey(String apiKey) {
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

    public ServiceBuilder httpClientConfig(HttpClientConfig httpClientConfig) {
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

    /**
     * Returns the fully configured {@link OAuthService}
     *
     * @param <S> OAuthService implementation (OAuth1/OAuth2/any API specific)
     * @param api will build Service for this API
     * @return fully configured {@link OAuthService}
     */
    public <S extends OAuthService> S build(BaseApi<S> api) {
        return api.createService(apiKey, apiSecret, callback, scope, debugStream, state, responseType, userAgent,
                httpClientConfig, httpClient);
    }
}
