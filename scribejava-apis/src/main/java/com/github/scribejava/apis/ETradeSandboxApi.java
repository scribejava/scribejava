package com.github.scribejava.apis;

public class ETradeSandboxApi extends ETradeApi {

    private static final String AUTHORIZE_URL = "https://us.etrade.com/e/t/etws/authorize";
    private static final String REQUEST_TOKEN_ENDPOINT = "https://apisb.etrade.com/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://apisb.etrade.com/oauth/access_token";

    protected ETradeSandboxApi() {
    }

    private static class InstanceHolder {
        private static final ETradeSandboxApi INSTANCE = new ETradeSandboxApi();
    }

    public static ETradeSandboxApi instance() {
        return ETradeSandboxApi.InstanceHolder.INSTANCE;
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
}
