package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.Token;

public class TokenExtractorTest {

    private TokenExtractorImpl extractor;

    @Before
    public void setup() {
        extractor = new TokenExtractorImpl();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() {
        String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenFromInvertedOAuthStandardResponse() {
        String response = "oauth_token_secret=hh5s93j4hdidpola&oauth_token=hdhd0244k9j7ao03";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getSecret());
        assertEquals("hdhd0244k9j7ao03", extracted.getToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithCallbackConfirmed() {
        String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03&callback_confirmed=true";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenWithEmptySecret() {
        String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("", extracted.getSecret());
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfTokenIsAbsent() {
        String response = "oauth_secret=hh5s93j4hdidpola&callback_confirmed=true";
        extractor.extract(response);
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfSecretIsAbsent() {
        String response = "oauth_token=hh5s93j4hdidpola&callback_confirmed=true";
        extractor.extract(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsNull() {
        String response = null;
        extractor.extract(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsEmptyString() {
        String response = "";
        extractor.extract(response);
    }
}
