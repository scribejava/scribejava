package com.github.scribejava.apis;

import com.github.scribejava.apis.service.AzureActiveDirectoryService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.utils.OAuthEncoder;

import java.util.Map;

/**
 * Microsoft Azure Active Directory Api
 *
 * Some helpful links
 * https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-protocols-oauth-code
 * https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-devquickstarts-webapp-java
 * https://msdn.microsoft.com/en-us/library/azure/ad/graph/api/signed-in-user-operations
 * https://portal.azure.com
 */
public class AzureActiveDirectoryApi extends DefaultApi20 {

    private static final String MSFT_GRAPH_URL = "https://graph.windows.net";

    private static final String AUTHORIZE_URL = "?client_id=%s&redirect_uri=%s&response_type=code&resource="
            + MSFT_GRAPH_URL;
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    private static final String MSFT_LOGIN_URL = "https://login.microsoftonline.com";
    private static final String SLASH = "/";
    private static final String COMMON = "common";
    private static final String TOKEN_URI = "oauth2/token";
    private static final String AUTH_URI = "oauth2/authorize";

    private static class InstanceHolder {

        private static final AzureActiveDirectoryApi INSTANCE = new AzureActiveDirectoryApi();
    }

    public static AzureActiveDirectoryApi instance() {
        return AzureActiveDirectoryApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + TOKEN_URI;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + AUTH_URI;
    }

    @Override
    public AzureActiveDirectoryService createService(OAuthConfig config) {
        return new AzureActiveDirectoryService(this, config);
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenJsonExtractor.instance();
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams) {

        final String scope = config.getScope();

        if (scope == null) {
            return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + AUTH_URI
                    + String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(),
                    OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
        } else {
            return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + AUTH_URI
                    + String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

}
