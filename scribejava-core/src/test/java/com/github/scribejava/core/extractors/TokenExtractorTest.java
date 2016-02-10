package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1Token;
import com.github.scribejava.core.model.Token;

public class TokenExtractorTest {

    private OAuth1AccessTokenExtractorImpl extractor;

    @Before
    public void setUp() {
        extractor = new OAuth1AccessTokenExtractorImpl();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() {
        final String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03";
        final Token extracted = extractor.extract(response);
        assertTrue(extracted instanceof OAuth1Token);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", ((OAuth1Token) extracted).getSecret());
    }

    @Test
    public void shouldExtractTokenFromInvertedOAuthStandardResponse() {
        final String response = "oauth_token_secret=hh5s93j4hdidpola&oauth_token=hdhd0244k9j7ao03";
        final Token extracted = extractor.extract(response);
        assertTrue(extracted instanceof OAuth1Token);
        assertEquals("hh5s93j4hdidpola", ((OAuth1Token) extracted).getSecret());
        assertEquals("hdhd0244k9j7ao03", extracted.getToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithCallbackConfirmed() {
        final String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03"
                + "&callback_confirmed=true";
        final Token extracted = extractor.extract(response);
        assertTrue(extracted instanceof OAuth1Token);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", ((OAuth1Token) extracted).getSecret());
    }

    @Test
    public void shouldExtractTokenWithEmptySecret() {
        final String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=";
        final Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("", ((OAuth1Token) extracted).getSecret());
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfTokenIsAbsent() {
        final String response = "oauth_secret=hh5s93j4hdidpola&callback_confirmed=true";
        extractor.extract(response);
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfSecretIsAbsent() {
        final String response = "oauth_token=hh5s93j4hdidpola&callback_confirmed=true";
        extractor.extract(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsNull() {
        extractor.extract(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsEmptyString() {
        final String response = "";
        extractor.extract(response);
    }
}
