package org.scribe.builder;

/**
 * Encapsulation for the Authorization url to cope with the weakness of the oauth2 spec.
 */
public interface AuthUrlBuilder {
    /**
     * The base url of the provider we should be redirecting the client to.
     *
     * @param endpoint provider endpoint url
     * @return builder instance
     */
    AuthUrlBuilder setEndpoint(String endpoint);

    /**
     * Your client id for this provider.
     *
     * @param clientId providers client id
     * @return builder instance
     */
    AuthUrlBuilder setClientId(String clientId);

    /**
     * Redirect/Callback url the provider should go after successful authorization.
     *
     * @param redirectUrl  redirect url
     * @return builder instance
     */
    AuthUrlBuilder setRedirectUrl(String redirectUrl);

    /**
     * Optional scopes to request for authorization.
     *
     * @param scope consult provider documentation for specifics on how these should be formatted
     * @return builder instance
     */
    AuthUrlBuilder setScope(String scope);

    /**
     * Optional state parameter that the provider will send as part of the response.
     * helps to protect against csrf attacks
     *
     * @param state random state
     * @return builder instance
     */
    AuthUrlBuilder setState(String state);

    /**
     * Optional response type, has no default, if none is provided it will not be part of the url
     *
     * @param responseType format of the authorization response
     * @return builder instance
     */
    AuthUrlBuilder setResponseType(String responseType);

    /**
     * The final authorization url.
     *
     * @return constructed url
     */
    String build();
}
