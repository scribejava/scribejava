package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth2.clientauthentication.HttpBasicAuthenticationScheme;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

/**
 * Represents<br>
 * 2.3. Client Authentication<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3 <br>
 * in it's part 2.3.1. Client Password<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3.1
 *
 * @deprecated use {@link com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication}
 */
@Deprecated
public enum ClientAuthenticationType {
    /**
     * @deprecated use {@link com.github.scribejava.core.oauth2.clientauthentication.HttpBasicAuthenticationScheme}
     */
    @Deprecated
    HTTP_BASIC_AUTHENTICATION_SCHEME {

        @Override
        public void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret) {
            HttpBasicAuthenticationScheme.instance().addClientAuthentication(request, apiKey, apiSecret);
        }
    },
    /**
     * @deprecated use {@link com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme}
     */
    @Deprecated
    REQUEST_BODY {
        @Override
        public void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret) {
            RequestBodyAuthenticationScheme.instance().addClientAuthentication(request, apiKey, apiSecret);
        }
    };

    public abstract void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret);
}
