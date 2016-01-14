package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;

public class TokenExtractor20Test {

    private OAuth2AccessTokenExtractorImpl extractor;

    @Before
    public void setup() {
        extractor = new OAuth2AccessTokenExtractorImpl();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() {
        String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE";
        Token extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE", extracted.getToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithExpiresParam() {
        String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires_in=5108";
        Token extracted = extractor.extract(response);
        assertTrue(extracted instanceof OAuth2AccessToken);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE", extracted.getToken());
        assertEquals(5108l, ((OAuth2AccessToken)extracted).getExpiresIn().longValue());
    }
    
    @Test
    public void shouldExtractTokenFromResponseWithExpiresAndRefreshParam() {
        String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires_in=5108&token_type=bearer&refresh_token=166942940015970";
        Token extracted = extractor.extract(response);
        assertTrue(extracted instanceof OAuth2AccessToken);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE", extracted.getToken());
        assertEquals(5108l, ((OAuth2AccessToken)extracted).getExpiresIn().longValue());
        assertEquals("bearer", ((OAuth2AccessToken)extracted).getTokenType());
        assertEquals("166942940015970", ((OAuth2AccessToken)extracted).getRefreshToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithManyParameters() {
        String response = "access_token=foo1234&other_stuff=yeah_we_have_this_too&number=42";
        Token extracted = extractor.extract(response);
        assertEquals("foo1234", extracted.getToken());
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfTokenIsAbsent() {
        String response = "&expires=5108";
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
