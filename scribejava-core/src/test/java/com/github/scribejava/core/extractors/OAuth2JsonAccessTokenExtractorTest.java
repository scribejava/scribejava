package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Daniel Tyreus
 */
public class OAuth2JsonAccessTokenExtractorTest {
    private String response = "'{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\","
            + "\"token_type\": \"Bearer\","
            + "\"refresh_token\": \"some_refresh_token\","
            + "\"expires_in\": 123456}'";
    private OAuth2JsonAccessTokenExtractor extractor = new OAuth2JsonAccessTokenExtractor();

    @Test
    public void shouldParseResponse() {
        Token token = extractor.extract(response);
        assertEquals("I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X", token.getToken());
    }
    
    @Test
    public void shouldParseRefreshableResponse() {
        Token token = extractor.extract(response);
        
        assertTrue(token instanceof OAuth2AccessToken);
        OAuth2AccessToken accessToken = (OAuth2AccessToken)token;
        
        assertEquals("I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X", accessToken.getToken());
        assertEquals("some_refresh_token", accessToken.getRefreshToken());
        assertEquals(123456l, accessToken.getExpiresIn().longValue());
        assertEquals("Bearer",accessToken.getTokenType());
    }
    
    @Test
    public void shouldParseNonStandardRefreshableResponse() {
        String response = "'{ \"access\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\","
            + "\"type\": \"Bearer\","
            + "\"refresh\": \"some_refresh_token\","
            + "\"expires_at\": 123456000}'";
        
        OAuth2JsonAccessTokenExtractor extractor = new OAuth2JsonAccessTokenExtractor("access","refresh",null,"expires_at","type");
        Token token = extractor.extract(response);
        
        assertTrue(token instanceof OAuth2AccessToken);
        OAuth2AccessToken accessToken = (OAuth2AccessToken)token;
        
        assertEquals("I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X", accessToken.getToken());
        assertEquals("some_refresh_token", accessToken.getRefreshToken());
        assertNotNull(accessToken.getExpiresIn());
        assertEquals("Bearer",accessToken.getTokenType());
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
