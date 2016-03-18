package com.github.scribejava.apis;

import com.github.scribejava.apis.openid.OpenIdConnectJsonTokenExtractor;
import com.github.scribejava.apis.service.OpenIdConnectServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OpenIdConnectConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;

public class OpenIdConnectApi extends DefaultApi20 {
    private final OpenIdConnectConfig config;

    public OpenIdConnectApi(OpenIdConnectConfig config) {
        this.config = config;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return config.getTokenEndpoint();
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        final StringBuilder sb = new StringBuilder(this.config.getAuthorizationEndpoint());

        sb.append("?response_type=").append(config.getResponseType());
        sb.append("&client_id=").append(config.getApiKey());
        sb.append("&redirect_uri=").append(OAuthEncoder.encode(config.getCallback()));
        sb.append("&scope=");
        final String scope = config.getScope();
        if (scope != null && !"".equals(scope)) {
            sb.append(OAuthEncoder.encode(scope));
        } else {
            final String[] scopes = this.config.getSupportedScopes();
            for (int i = 0; i < scopes.length; i++) {
                if (i > 0) {
                    sb.append("%20");
                }
                sb.append(scopes[i]);
            }
        }

        final String state = config.getState();
        if (state != null) {
            sb.append("&").append(OAuthConstants.STATE).append("=").append(OAuthEncoder.encode(state));
        }

        return sb.toString();
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OpenIdConnectJsonTokenExtractor.instance();
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new OpenIdConnectServiceImpl(this, config);
    }

}
