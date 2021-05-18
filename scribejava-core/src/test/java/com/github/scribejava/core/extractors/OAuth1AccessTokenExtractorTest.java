package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.Response;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1Token;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.function.ThrowingRunnable;

public class OAuth1AccessTokenExtractorTest {

    private OAuth1AccessTokenExtractor extractor;

    @Before
    public void setUp() {
        extractor = OAuth1AccessTokenExtractor.instance();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() throws IOException {
        final String responseBody = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03";
        final OAuth1Token extracted;
        try (Response response = ok(responseBody)) {
            extracted = extractor.extract(response);
        }
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", extracted.getTokenSecret());
    }

    @Test
    public void shouldExtractTokenFromInvertedOAuthStandardResponse() throws IOException {
        final String responseBody = "oauth_token_secret=hh5s93j4hdidpola&oauth_token=hdhd0244k9j7ao03";
        final OAuth1Token extracted;
        try (Response response = ok(responseBody)) {
            extracted = extractor.extract(response);
        }
        assertEquals("hh5s93j4hdidpola", extracted.getTokenSecret());
        assertEquals("hdhd0244k9j7ao03", extracted.getToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithCallbackConfirmed() throws IOException {
        final String responseBody = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03"
                + "&callback_confirmed=true";
        final OAuth1Token extracted;
        try (Response response = ok(responseBody)) {
            extracted = extractor.extract(response);
        }
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", extracted.getTokenSecret());
    }

    @Test
    public void shouldExtractTokenWithEmptySecret() throws IOException {
        final String responseBody = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=";
        final OAuth1Token extracted;
        try (Response response = ok(responseBody)) {
            extracted = extractor.extract(response);
        }
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("", extracted.getTokenSecret());
    }

    public void shouldThrowExceptionIfTokenIsAbsent() throws IOException {
        final String responseBody = "oauth_secret=hh5s93j4hdidpola&callback_confirmed=true";
        try (Response response = ok(responseBody)) {
            assertThrows(OAuthException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    extractor.extract(response);
                }
            });
        }
    }

    public void shouldThrowExceptionIfSecretIsAbsent() throws IOException {
        final String responseBody = "oauth_token=hh5s93j4hdidpola&callback_confirmed=true";
        try (Response response = ok(responseBody)) {
            assertThrows(OAuthException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    extractor.extract(response);
                }
            });
        }
    }

    public void shouldThrowExceptionIfResponseIsNull() throws IOException {
        try (Response response = ok(null)) {
            assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    extractor.extract(response);
                }
            });
        }
    }

    public void shouldThrowExceptionIfResponseIsEmptyString() throws IOException {
        final String responseBody = "";
        try (Response response = ok(responseBody)) {
            assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    extractor.extract(response);
                }
            });
        }
    }

    private static Response ok(String body) {
        return new Response(200, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }
}
