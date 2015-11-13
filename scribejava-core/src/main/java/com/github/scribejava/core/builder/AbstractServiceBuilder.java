package com.github.scribejava.core.builder;

import java.io.OutputStream;
import com.github.scribejava.core.builder.api.Api;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.SignatureType;
import com.github.scribejava.core.utils.Preconditions;

abstract class AbstractServiceBuilder<T extends AbstractServiceBuilder> {

    private Api api;
    private String callback;
    private String apiKey;
    private String apiSecret;
    private String scope;
    private String state;
    private SignatureType signatureType;
    private OutputStream debugStream;
    private String grantType;

    public AbstractServiceBuilder() {
        this.callback = OAuthConstants.OUT_OF_BAND;
        this.signatureType = SignatureType.Header;
    }

    /**
     * Configures the {@link Api}
     *
     * @param apiClass the class of one of the existent {@link Api}s on org.scribe.api package
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T provider(final Class<? extends Api> apiClass) {
        Preconditions.checkNotNull(apiClass, "Api class cannot be null");
        try {
            api = apiClass.newInstance();
        } catch (IllegalAccessException | InstantiationException | RuntimeException e) {
            throw new OAuthException("Error while creating the Api object", e);
        }
        return (T) this;
    }

    /**
     * Configures the {@link Api} Overloaded version. Let's you use an instance instead of a class.
     *
     * @param api instance of {@link Api}s
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T provider(final Api api) {
        Preconditions.checkNotNull(api, "Api cannot be null");
        this.api = api;
        return (T) this;
    }

    /**
     * Adds an OAuth callback url
     *
     * @param callback callback url. Must be a valid url or 'oob' for out of band OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T callback(final String callback) {
        Preconditions.checkNotNull(callback, "Callback can't be null");
        this.callback = callback;
        return (T) this;
    }

    /**
     * Configures the api key
     *
     * @param apiKey The api key for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T apiKey(final String apiKey) {
        Preconditions.checkEmptyString(apiKey, "Invalid Api key");
        this.apiKey = apiKey;
        return (T) this;
    }

    /**
     * Configures the api secret
     *
     * @param apiSecret The api secret for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T apiSecret(final String apiSecret) {
        Preconditions.checkEmptyString(apiSecret, "Invalid Api secret");
        this.apiSecret = apiSecret;
        return (T) this;
    }

    /**
     * Configures the OAuth scope. This is only necessary in some APIs (like Google's).
     *
     * @param scope The OAuth scope
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T scope(final String scope) {
        Preconditions.checkEmptyString(scope, "Invalid OAuth scope");
        this.scope = scope;
        return (T) this;
    }

    /**
     * Configures the anti forgery session state. This is available in some APIs (like Google's).
     *
     * @param state The OAuth state
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T state(final String state) {
        Preconditions.checkEmptyString(state, "Invalid OAuth state");
        this.state = state;
        return (T) this;
    }

    /**
     * Configures the signature type, choose between header, querystring, etc. Defaults to Header
     *
     * @param type SignatureType
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public T signatureType(final SignatureType type) {
        Preconditions.checkNotNull(type, "Signature type can't be null");
        this.signatureType = type;
        return (T) this;
    }

    public T debugStream(final OutputStream stream) {
        Preconditions.checkNotNull(stream, "debug stream can't be null");
        this.debugStream = stream;
        return (T) this;
    }

    public T grantType(final String grantType) {
        Preconditions.checkEmptyString(grantType, "Invalid OAuth grantType");
        this.grantType = grantType;
        return (T) this;
    }

    public T debug() {
        debugStream(System.out);
        return (T) this;
    }

    public void checkPreconditions() {
        Preconditions.checkNotNull(api, "You must specify a valid api through the provider() method");
        Preconditions.checkEmptyString(apiKey, "You must provide an api key");
        Preconditions.checkEmptyString(apiSecret, "You must provide an api secret");
    }

    public Api getApi() {
        return api;
    }

    public String getCallback() {
        return callback;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getScope() {
        return scope;
    }

    public String getState() {
        return state;
    }

    public SignatureType getSignatureType() {
        return signatureType;
    }

    public OutputStream getDebugStream() {
        return debugStream;
    }

    public String getGrantType() {
        return grantType;
    }
}
