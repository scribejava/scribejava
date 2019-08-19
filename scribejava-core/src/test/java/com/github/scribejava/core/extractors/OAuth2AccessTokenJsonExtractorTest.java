package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth2.OAuth2Error;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OAuth2AccessTokenJsonExtractorTest {

    private final OAuth2AccessTokenJsonExtractor extractor = OAuth2AccessTokenJsonExtractor.instance();

    @Test
    public void shouldParseResponse() throws IOException {
        final String responseBody = "{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\", "
                + "\"token_type\":\"example\"}";
        final OAuth2AccessToken token;
        try (Response response = ok(responseBody)) {
            token = extractor.extract(response);
        }
        assertEquals("I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X", token.getAccessToken());
    }

    @Test
    public void shouldParseScopeFromResponse() throws IOException {
        final String responseBody = "{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T4X\", "
                + "\"token_type\":\"example\","
                + "\"scope\":\"s1\"}";
        final OAuth2AccessToken token;
        try (Response response = ok(responseBody)) {
            token = extractor.extract(response);
        }
        assertEquals("I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T4X", token.getAccessToken());
        assertEquals("s1", token.getScope());

        final String responseBody2 = "{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T5X\", "
                + "\"token_type\":\"example\","
                + "\"scope\":\"s1 s2\"}";
        final OAuth2AccessToken token2;
        try (Response response = ok(responseBody2)) {
            token2 = extractor.extract(response);
        }
        assertEquals("I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T5X", token2.getAccessToken());
        assertEquals("s1 s2", token2.getScope());

        final String responseBody3 = "{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T6X\", "
                + "\"token_type\":\"example\","
                + "\"scope\":\"s3 s4\", "
                + "\"refresh_token\":\"refresh_token1\"}";
        final OAuth2AccessToken token3;
        try (Response response = ok(responseBody3)) {
            token3 = extractor.extract(response);
        }
        assertEquals("I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T6X", token3.getAccessToken());
        assertEquals("s3 s4", token3.getScope());
        assertEquals("refresh_token1", token3.getRefreshToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForNullParameters() throws IOException {
        try (Response response = ok(null)) {
            extractor.extract(response);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForEmptyStrings() throws IOException {
        final String responseBody = "";
        try (Response response = ok(responseBody)) {
            extractor.extract(response);
        }
    }

    @Test
    public void shouldThrowExceptionIfResponseIsError() throws IOException {
        final String responseBody = "{"
                + "\"error_description\":\"unknown, invalid, or expired refresh token\","
                + "\"error\":\"invalid_grant\""
                + "}";
        try (Response response = error(responseBody)) {
            extractor.extract(response);
            fail();
        } catch (OAuth2AccessTokenErrorResponse oaer) {
            assertEquals(OAuth2Error.INVALID_GRANT, oaer.getError());
            assertEquals("unknown, invalid, or expired refresh token", oaer.getErrorDescription());
        }
    }

    @Test
    public void testEscapedJsonInResponse() throws IOException {
        final String responseBody = "{ \"access_token\":\"I0122HKLEM2\\/MV3ABKFTDT3T5X\","
                + "\"token_type\":\"example\"}";
        final OAuth2AccessToken token;
        try (Response response = ok(responseBody)) {
            token = extractor.extract(response);
        }
        assertEquals("I0122HKLEM2/MV3ABKFTDT3T5X", token.getAccessToken());
    }

    private static Response ok(String body) {
        return new Response(200, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }

    private static Response error(String body) {
        return new Response(400, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }
}
