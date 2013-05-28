package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class Paypal2Api1 extends DefaultApi20 {

    private static final String AUTHORIZATION_URL =
            "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize?client_id=%s&response_type=code&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZATION_URL + "&scope=%s";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/tokenservice?grant_type=authorization_code";

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    /**
     * Returns the verb for the access token endpoint (defaults to GET)
     *
     * @return access token endpoint verb
     */
    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Paypal does not support OOB");

        // Append scope if present
        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), config.getScope());
        } else {
            return String.format(AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    public static class Sandbox extends Paypal2Api1 {

        private static final String SANDBOX_AUTHORIZATION_URL =
                "https://www.sandbox.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize?client_id=%s&response_type=code&redirect_uri=%s";
        private static final String SANDBOX_SCOPED_AUTHORIZE_URL = SANDBOX_AUTHORIZATION_URL + "&scope=%s";
        private static final String SANDBOX_ACCESS_TOKEN_ENDPOINT = "https://www.sandbox.paypal.com/webapps/auth/protocol/openidconnect/v1/tokenservice?grant_type=authorization_code";

        @Override
        public String getAccessTokenEndpoint() {
            return SANDBOX_ACCESS_TOKEN_ENDPOINT;
        }

        @Override
        public String getAuthorizationUrl(OAuthConfig config) {
            Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Paypal does not support OOB");

            // Append scope if present
            if (config.hasScope()) {
                return String.format(SANDBOX_SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), config.getScope());
            } else {
                return String.format(SANDBOX_AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
            }
        }
    }
}
