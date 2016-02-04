package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.Token;

public class TokenExtractor20Test {

    private TokenExtractor20Impl extractor;

    @Before
    public void setUp() {
        extractor = new TokenExtractor20Impl();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE";
        final Token extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getToken());
        assertEquals("", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenFromResponseWithExpiresParam() {
        final String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159"
                + "|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires=5108";
        final Token extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getToken());
        assertEquals("", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenFromResponseWithManyParameters() {
        final String response = "access_token=foo1234&other_stuff=yeah_we_have_this_too&number=42";
        final Token extracted = extractor.extract(response);
        assertEquals("foo1234", extracted.getToken());
        assertEquals("", extracted.getSecret());
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
