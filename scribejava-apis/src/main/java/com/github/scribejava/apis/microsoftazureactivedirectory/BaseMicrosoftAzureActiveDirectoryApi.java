package com.github.scribejava.apis.microsoftazureactivedirectory;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public abstract class BaseMicrosoftAzureActiveDirectoryApi extends DefaultApi20 {

    private static final String MSFT_LOGIN_URL = "https://login.microsoftonline.com";
    private static final String SLASH = "/";
    private static final String COMMON = "common";
    private static final String TOKEN_URI = "oauth2/token";
    private final String resource;
    private final BaseMicrosoftAzureActiveDirectoryBearerSignature bearerSignature;

    protected BaseMicrosoftAzureActiveDirectoryApi(String resource,
            BaseMicrosoftAzureActiveDirectoryBearerSignature bearerSignature) {
        this.resource = resource;
        this.bearerSignature = bearerSignature;
    }

    protected BaseMicrosoftAzureActiveDirectoryApi(MicrosoftAzureActiveDirectoryVersion version) {
        this.resource = version.getResource();
        this.bearerSignature = version.getBearerSignature();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + TOKEN_URI;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return MSFT_LOGIN_URL + SLASH + COMMON + SLASH + "oauth2/authorize?resource=" + resource;
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    @Override
    public BearerSignature getBearerSignature() {
        return bearerSignature;
    }

    protected enum MicrosoftAzureActiveDirectoryVersion {
        V_1_0("https://graph.windows.net", MicrosoftAzureActiveDirectoryBearerSignature.instance()),
        V_2_0("https://graph.microsoft.com", MicrosoftAzureActiveDirectory20BearerSignature.instance());

        private final String resource;
        private final BaseMicrosoftAzureActiveDirectoryBearerSignature bearerSignature;

        MicrosoftAzureActiveDirectoryVersion(String resource,
                BaseMicrosoftAzureActiveDirectoryBearerSignature bearerSignature) {
            this.resource = resource;
            this.bearerSignature = bearerSignature;
        }

        protected String getResource() {
            return resource;
        }

        protected BaseMicrosoftAzureActiveDirectoryBearerSignature getBearerSignature() {
            return bearerSignature;
        }
    }
}
