package com.github.scribejava.apis;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.HttpBasicAuthenticationScheme;
import com.github.scribejava.core.oauth2.clientauthentication.JWTAuthenticationScheme;

import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class KeycloakApi extends DefaultApi20 {

    private static final ConcurrentMap<String, KeycloakApi> INSTANCES = new ConcurrentHashMap<>();

    private final String baseUrlWithRealm;
    private final RSAPrivateKey privateKey;
    private final String keyId;

    protected KeycloakApi(String baseUrlWithRealm, RSAPrivateKey privateKey, String keyId) {
        this.baseUrlWithRealm = baseUrlWithRealm;
        this.privateKey = privateKey;
        this.keyId = keyId;
    }

    public static KeycloakApi instance() {
        return instance("http://localhost:8080/", "master");
    }

    public static KeycloakApi instance(String baseUrl, String realm) {
        return instance(baseUrl, realm, null, null);
    }

    public static KeycloakApi instance(String baseUrl, String realm, RSAPrivateKey privateKey, String keyId) {
        final String defaultBaseUrlWithRealm = composeBaseUrlWithRealm(baseUrl, realm);

        //java8: switch to ConcurrentMap::computeIfAbsent
        KeycloakApi api = INSTANCES.get(defaultBaseUrlWithRealm);
        if (api == null) {
            api = new KeycloakApi(defaultBaseUrlWithRealm, privateKey, keyId);
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

    @Override
    public ClientAuthentication getClientAuthentication() {
        if (this.keyId != null && this.privateKey != null) {
            return JWTAuthenticationScheme.instance(privateKey, baseUrlWithRealm, keyId);
        }
        return HttpBasicAuthenticationScheme.instance();
    }
}
