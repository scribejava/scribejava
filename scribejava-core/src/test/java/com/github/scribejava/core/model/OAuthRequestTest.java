package com.github.scribejava.core.model;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "http://example.com", new OAuthConfig("test", "test"));
    }

    @Test
    public void shouldAddOAuthParamters() {
        request.addOAuthParameter(OAuthConstants.TOKEN, "token");
        request.addOAuthParameter(OAuthConstants.NONCE, "nonce");
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "ts");
        request.addOAuthParameter(OAuthConstants.SCOPE, "feeds");
        request.addOAuthParameter(OAuthConstants.REALM, "some-realm");

        assertEquals(5, request.getOauthParameters().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfParameterIsNotOAuth() {
        request.addOAuthParameter("otherParam", "value");
    }
}
