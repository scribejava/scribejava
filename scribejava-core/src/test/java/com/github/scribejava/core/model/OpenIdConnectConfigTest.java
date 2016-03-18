package com.github.scribejava.core.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.net.URL;

import org.junit.Test;

public class OpenIdConnectConfigTest {
    private static final byte[] CFG = ("{\"authorization_endpoint\":\"https://localhost:8080/oauth2/authorize\","
            + "\"token_endpoint\":\"https://localhost:8080/oauth2/token\",\"token_endpoint_auth_methods_supported\":"
            + "[\"client_secret_post\",\"private_key_jwt\"],\"jwks_uri\":\"https://localhost:8080/discovery/keys\","
            + "\"response_modes_supported\":[\"query\",\"fragment\",\"form_post\"],\"subject_types_supported\":"
            + "[\"pairwise\"],\"id_token_signing_alg_values_supported\":[\"RS256\"],\"http_logout_supported\":true,"
            + "\"response_types_supported\":[\"code\",\"id_token\",\"code id_token\",\"token id_token\",\"token\"],"
            + "\"scopes_supported\":[\"openid\"],\"issuer\":\"https://localhost:8080/oauth2/{tenantid}/\","
            + "\"claims_supported\":[\"sub\",\"iss\",\"aud\",\"exp\",\"iat\",\"auth_time\",\"acr\",\"amr\",\"nonce\","
            + "\"email\",\"given_name\",\"family_name\",\"nickname\"],\"microsoft_multi_refresh_token\":true,"
            + "\"check_session_iframe\":\"https://localhost:8080/oauth2/checksession\",\"end_session_endpoint\":"
            + "\"https://localhost:8080/oauth2/logout\",\"userinfo_endpoint\":"
            + "\"https://localhost:8080/openid/userinfo\"}").getBytes();

    @Test
    public void shouldReadOpenIdConnectConfiguration() throws Exception {
        final OpenIdConnectConfig cfg = OpenIdConnectConfig.read(new ByteArrayInputStream(CFG));
        assertNotNull("Expected configuration to be read from remote URL!", cfg);

        assertEquals("https://localhost:8080/oauth2/authorize", cfg.getAuthorizationEndpoint());
        assertEquals("https://localhost:8080/oauth2/token", cfg.getTokenEndpoint());
        assertArrayEquals(new String[] { "openid" }, cfg.getSupportedScopes());
    }

    @Test
    public void shouldReadGoogleOpenIdConnectConfigurationFromURL() throws Exception {
        final URL url = new URL("https://accounts.google.com/.well-known/openid-configuration");
        final OpenIdConnectConfig cfg = OpenIdConnectConfig.readFromURL(url);
        assertNotNull("Expected configuration to be read from remote URL!", cfg);
    }

    @Test
    public void shouldReadOfficeOpenIdConnectConfigurationFromURL() throws Exception {
        final URL url = new URL("https://login.microsoftonline.com/common/.well-known/openid-configuration");
        final OpenIdConnectConfig cfg = OpenIdConnectConfig.readFromURL(url);
        assertNotNull("Expected configuration to be read from remote URL!", cfg);
    }
}
