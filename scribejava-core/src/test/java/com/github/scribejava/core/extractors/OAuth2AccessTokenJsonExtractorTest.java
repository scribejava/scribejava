package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OAuth2AccessTokenJsonExtractorTest {

    private static final String RESPONSE = "'{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\"}'";
    private final OAuth2AccessTokenJsonExtractor extractor = OAuth2AccessTokenJsonExtractor.instance();

    @Test
    public void shouldParseResponse() throws IOException {
        final OAuth2AccessToken token = extractor.extract(ok(RESPONSE));
        assertEquals(token.getAccessToken(), "I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForNullParameters() throws IOException {
        extractor.extract(ok(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForEmptyStrings() throws IOException {
        extractor.extract(ok(""));
    }

    @Test
    public void shouldThrowExceptionIfResponseIsError() throws IOException {
        final String body = "{" +
                "\"error_description\":\"unknown, invalid, or expired refresh token\"," +
                "\"error\":\"invalid_grant\"" +
                "}";
        try {
            extractor.extract(error(body));
            fail();
        } catch (OAuth2AccessTokenErrorResponse oaer) {
            assertEquals(OAuth2AccessTokenErrorResponse.ErrorCode.invalid_grant, oaer.getErrorCode());
            assertEquals("unknown, invalid, or expired refresh token", oaer.getErrorDescription());
        }
    }

    private static Response ok(String body) {
        return new Response(200, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }

    private static Response error(String body) {
        return new Response(400, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }
}
