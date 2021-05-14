package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;

public class ETradeApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://us.etrade.com/e/t/etws/authorize";
    private static final String REQUEST_TOKEN_ENDPOINT = "https://api.etrade.com/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://api.etrade.com/oauth/access_token";

    protected ETradeApi() {
    }

    private static class InstanceHolder {

        private static final ETradeApi INSTANCE = new ETradeApi();
    }

    public static ETradeApi instance() {
        return ETradeApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_ENDPOINT;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    /**
     * @return the signature type, choose between header, querystring, etc. Defaults to Header
     */
    @Override
    public OAuth1SignatureType getSignatureType() {
        return OAuth1SignatureType.QUERY_STRING;
    }
}
