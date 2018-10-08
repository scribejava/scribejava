package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * @deprecated doesn't do anything useful, all MicrosoftAzureActiveDirectory specific logic was moved to
 * {@link com.github.scribejava.apis.microsoftazureactivedirectory.MicrosoftAzureActiveDirectoryBearerSignature}
 */
@Deprecated
public class MicrosoftAzureActiveDirectoryService extends OAuth20Service {

    public MicrosoftAzureActiveDirectoryService(DefaultApi20 api, String apiKey, String apiSecret, String callback,
            String scope, String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    /**
     * @deprecated moved to
     * {@link com.github.scribejava.apis.microsoftazureactivedirectory.MicrosoftAzureActiveDirectoryBearerSignature}
     */
    @Deprecated
    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
    }
}
