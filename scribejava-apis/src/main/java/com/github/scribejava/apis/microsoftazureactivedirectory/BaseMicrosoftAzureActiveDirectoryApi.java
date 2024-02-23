package com.github.scribejava.apis.microsoftazureactivedirectory;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public abstract class BaseMicrosoftAzureActiveDirectoryApi extends DefaultApi20 {

    protected static final String COMMON_TENANT = "common";
    protected static final String COMMON_LOGIN_URL = "https://login.microsoftonline.com/";

    private static final String OAUTH_2 = "/oauth2";
    private final String login_url;
    private final String tenant;

    protected BaseMicrosoftAzureActiveDirectoryApi() {
        this(COMMON_TENANT);
    }

    protected BaseMicrosoftAzureActiveDirectoryApi(String tenant) {
        this.tenant = tenant == null || tenant.isEmpty() ? COMMON_TENANT : tenant;
        this.login_url = COMMON_LOGIN_URL;
    }

    protected BaseMicrosoftAzureActiveDirectoryApi(String login_url, String tenant) {
        this(tenant);
        this.login_url = login_url == null || login_url.isEmpty() ? COMMON_LOGIN_URL : login_url;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return login_url + tenant + OAUTH_2 + getEndpointVersionPath() + "/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return login_url + tenant + OAUTH_2 + getEndpointVersionPath() + "/authorize";
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    protected String getEndpointVersionPath() {
        return "";
    }
}
