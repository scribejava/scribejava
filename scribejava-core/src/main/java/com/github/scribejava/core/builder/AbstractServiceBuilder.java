package com.github.scribejava.core.builder;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.OAuthConfig;
import java.io.OutputStream;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.SignatureType;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.core.utils.Preconditions;

abstract class AbstractServiceBuilder<T extends AbstractServiceBuilder<T>> {

    private String callback;
    private String apiKey;
    private String apiSecret;
    private String scope;
    private String state;
    private SignatureType signatureType;
    private OutputStream debugStream;
    private String grantType;
    private String responseType = "code";

    AbstractServiceBuilder() {
        this.callback = OAuthConstants.OUT_OF_BAND;
        this.signatureType = SignatureType.Header;
    }

    /**
     * Adds an OAuth callback url
     *
     * @param callback callback url. Must be a valid url or 'oob' for out of band OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    @SuppressWarnings("unchecked")
    public T callback(String callback) {
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
    @SuppressWarnings("unchecked")
    public T apiKey(String apiKey) {
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
    @SuppressWarnings("unchecked")
    public T apiSecret(String apiSecret) {
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
    @SuppressWarnings("unchecked")
    public T scope(String scope) {
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
    @SuppressWarnings("unchecked")
    public T state(String state) {
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
    @SuppressWarnings("unchecked")
    public T signatureType(SignatureType type) {
        Preconditions.checkNotNull(type, "Signature type can't be null");
        this.signatureType = type;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T debugStream(OutputStream stream) {
        Preconditions.checkNotNull(stream, "debug stream can't be null");
        this.debugStream = stream;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T grantType(String grantType) {
        Preconditions.checkEmptyString(grantType, "Invalid OAuth grantType");
        this.grantType = grantType;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T responseType(String responseType) {
        Preconditions.checkEmptyString(responseType, "Invalid OAuth responseType");
        this.responseType = responseType;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T debug() {
        debugStream(System.out);
        return (T) this;
    }

    public void checkPreconditions() {
        Preconditions.checkEmptyString(apiKey, "You must provide an api key");
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

    public String getResponseType() {
        return responseType;
    }

    protected abstract OAuthConfig createConfig();

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
