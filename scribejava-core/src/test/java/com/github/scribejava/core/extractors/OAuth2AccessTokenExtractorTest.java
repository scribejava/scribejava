package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.Response;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class OAuth2AccessTokenExtractorTest {

    private OAuth2AccessTokenExtractor extractor;

    @Before
    public void setUp() {
        extractor = OAuth2AccessTokenExtractor.instance();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() throws IOException {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE";
        final OAuth2AccessToken extracted = extractor.extract(ok(response));
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getAccessToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithExpiresParam() throws IOException {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires_in=5108";
        final OAuth2AccessToken extracted = extractor.extract(ok(response));
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getAccessToken());
        assertEquals(Integer.valueOf(5108), extracted.getExpiresIn());
    }

    @Test
    public void shouldExtractTokenFromResponseWithExpiresAndRefreshParam() throws IOException {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires_in=5108&token_type=bearer&refresh_token=166942940015970";
        final OAuth2AccessToken extracted = extractor.extract(ok(response));
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getAccessToken());
        assertEquals(Integer.valueOf(5108), extracted.getExpiresIn());
        assertEquals("bearer", extracted.getTokenType());
        assertEquals("166942940015970", extracted.getRefreshToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithManyParameters() throws IOException {
        final String response = "access_token=foo1234&other_stuff=yeah_we_have_this_too&number=42";
        final OAuth2AccessToken extracted = extractor.extract(ok(response));
        assertEquals("foo1234", extracted.getAccessToken());
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfErrorResponse() throws IOException {
        final String response = "";
        extractor.extract(error(response));
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfTokenIsAbsent() throws IOException {
        final String response = "&expires=5108";
        extractor.extract(ok(response));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsNull() throws IOException {
        extractor.extract(ok(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsEmptyString() throws IOException {
        final String response = "";
        extractor.extract(ok(response));
    }

    private static Response ok(String body) {
        return new Response(200, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }

    private static Response error(String body) {
        return new Response(400, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }
}
