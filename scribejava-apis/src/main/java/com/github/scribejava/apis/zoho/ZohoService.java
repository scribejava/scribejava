package com.github.scribejava.apis.zoho;

import com.github.scribejava.apis.ZohoApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.DeviceAuthorization;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.OutputStream;

public class ZohoService extends OAuth20Service {
    public ZohoService(ZohoApi20 api, String apiKey, String apiSecret, String callback, String defaultScope,
                       String responseType, OutputStream debugStream, String userAgent,
                       HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent,
                httpClientConfig, httpClient);
    }

    @Override
    protected OAuthRequest createAccessTokenPasswordGrantRequest(String username, String password, String scope) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected OAuthRequest createAccessTokenClientCredentialsGrantRequest(String scope) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected OAuthRequest createAccessTokenDeviceAuthorizationGrantRequest(DeviceAuthorization deviceAuthorization) {
        throw new UnsupportedOperationException();
    }
}
