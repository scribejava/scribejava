package com.github.scribejava.core.extractors;

import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import static org.junit.Assert.assertEquals;

public class OAuth2AccessTokenExtractorTest {

    private OAuth2AccessTokenExtractor extractor;

    @Before
    public void setUp() {
        extractor = OAuth2AccessTokenExtractor.instance();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE";
        final OAuth2AccessToken extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getAccessToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithExpiresParam() {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires_in=5108";
        final OAuth2AccessToken extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getAccessToken());
        assertEquals(Integer.valueOf(5108), extracted.getExpiresIn());
    }

    @Test
    public void shouldExtractTokenFromResponseWithExpiresAndRefreshParam() {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires_in=5108&token_type=bearer&refresh_token=166942940015970";
        final OAuth2AccessToken extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getAccessToken());
        assertEquals(Integer.valueOf(5108), extracted.getExpiresIn());
        assertEquals("bearer", extracted.getTokenType());
        assertEquals("166942940015970", extracted.getRefreshToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithManyParameters() {
        final String response = "access_token=foo1234&other_stuff=yeah_we_have_this_too&number=42";
        final OAuth2AccessToken extracted = extractor.extract(response);
        assertEquals("foo1234", extracted.getAccessToken());
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfTokenIsAbsent() {
        final String response = "&expires=5108";
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
