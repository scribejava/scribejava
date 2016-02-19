package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OAuth2AccessTokenJsonExtractorTest {

    private static final String RESPONSE = "'{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\"}'";
    private final OAuth2AccessTokenJsonExtractor extractor = OAuth2AccessTokenJsonExtractor.instance();

    @Test
    public void shouldParseResponse() {
        final OAuth2AccessToken token = extractor.extract(RESPONSE);
        assertEquals(token.getAccessToken(), "I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForNullParameters() {
        extractor.extract(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForEmptyStrings() {
        extractor.extract("");
    }
}
