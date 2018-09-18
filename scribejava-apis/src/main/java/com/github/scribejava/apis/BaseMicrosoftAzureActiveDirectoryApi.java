package com.github.scribejava.apis;

import com.github.scribejava.apis.service.MicrosoftAzureActiveDirectoryService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

import java.io.OutputStream;
abstract class BaseMicrosoftAzureActiveDirectoryApi extends DefaultApi20 {

    private static final String MSFT_LOGIN_URL = "https://login.microsoftonline.com";
    private static final String SLASH = "/";
    private static final String COMMON = "common";
    private static final String TOKEN_URI = "oauth2/token";
    private final MicrosoftAzureActiveDirectoryVersion microsoftAzureActiveDirectoryVersion;

    BaseMicrosoftAzureActiveDirectoryApi(MicrosoftAzureActiveDirectoryVersion version) {
        this.microsoftAzureActiveDirectoryVersion = version;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + TOKEN_URI;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + microsoftAzureActiveDirectoryVersion.getAuthUri();
    }

    @Override
    public MicrosoftAzureActiveDirectoryService createService(String apiKey, String apiSecret, String callback,
                                                              String scope, OutputStream debugStream, String state,
                                                              String responseType, String userAgent,
                                                              HttpClientConfig httpClientConfig, HttpClient httpClient
    ) {
        return new MicrosoftAzureActiveDirectoryService(this, apiKey, apiSecret, callback, scope, state, responseType,
                userAgent, httpClientConfig, httpClient, microsoftAzureActiveDirectoryVersion.getAcceptedFormat());
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    public enum MicrosoftAzureActiveDirectoryVersion {
        V_1_0("https://graph.windows.net", "application/json; odata=minimalmetadata; streaming=true; charset=utf-8"),
        V_2_0("https://graph.microsoft.com", "application/json");

        private final String resource;
        private final String acceptedFormat;

        MicrosoftAzureActiveDirectoryVersion(String resource, String acceptedFormat) {
            this.resource = resource;
            this.acceptedFormat = acceptedFormat;
        }

        public String getAuthUri() {
            return "oauth2/authorize?resource=" + resource;
        }

        public String getAcceptedFormat() {
            return acceptedFormat;

        }
    }
}
