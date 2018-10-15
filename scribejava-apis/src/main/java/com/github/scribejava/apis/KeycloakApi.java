package com.github.scribejava.apis;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class KeycloakApi extends DefaultApi20 {

    private static final ConcurrentMap<String, KeycloakApi> INSTANCES = new ConcurrentHashMap<>();

    private final String baseUrlWithRealm;

    protected KeycloakApi(String baseUrlWithRealm) {
        this.baseUrlWithRealm = baseUrlWithRealm;
    }

    public static KeycloakApi instance() {
        return instance("http://localhost:8080/", "master");
    }

    public static KeycloakApi instance(String baseUrl, String realm) {
        final String defaultBaseUrlWithRealm = composeBaseUrlWithRealm(baseUrl, realm);

        //java8: switch to ConcurrentMap::computeIfAbsent
        KeycloakApi api = INSTANCES.get(defaultBaseUrlWithRealm);
        if (api == null) {
            api = new KeycloakApi(defaultBaseUrlWithRealm);
            final KeycloakApi alreadyCreatedApi = INSTANCES.putIfAbsent(defaultBaseUrlWithRealm, api);
            if (alreadyCreatedApi != null) {
                return alreadyCreatedApi;
            }
        }
        return api;
    }

    protected static String composeBaseUrlWithRealm(String baseUrl, String realm) {
        return baseUrl + (baseUrl.endsWith("/") ? "" : "/") + "auth/realms/" + realm;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return baseUrlWithRealm + "/protocol/openid-connect/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return baseUrlWithRealm + "/protocol/openid-connect/auth";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OpenIdJsonTokenExtractor.instance();
    }

    @Override
    public String getRevokeTokenEndpoint() {
        throw new RuntimeException("Not implemented yet");
    }
}
