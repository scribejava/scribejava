package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;

/**
 * @deprecated moved to {@link com.github.scribejava.apis.odnoklassniki.OdnoklassnikiOAuthService}
 */
@Deprecated
public class OdnoklassnikiOAuthService extends com.github.scribejava.apis.odnoklassniki.OdnoklassnikiOAuthService {

    public OdnoklassnikiOAuthService(DefaultApi20 api, String apiKey, String apiSecret, String callback, String scope,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

}
