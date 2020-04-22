package com.github.scribejava.core.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "http://example.com");
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

    @Test
    public void shouldNotSentHeaderTwice() {
        assertTrue(request.getHeaders().isEmpty());
        request.addHeader("HEADER-NAME", "first");
        request.addHeader("header-name", "middle");
        request.addHeader("Header-Name", "last");

        assertEquals(1, request.getHeaders().size());

        assertTrue(request.getHeaders().containsKey("HEADER-NAME"));
        assertTrue(request.getHeaders().containsKey("header-name"));
        assertTrue(request.getHeaders().containsKey("Header-Name"));

        assertEquals("last", request.getHeaders().get("HEADER-NAME"));
        assertEquals("last", request.getHeaders().get("header-name"));
        assertEquals("last", request.getHeaders().get("Header-Name"));
    }
    @Test
    public void shouldAddMultipleHeaders() {
        final Map<String, String> dummyHeaderMap = new HashMap<>(2);
        dummyHeaderMap.put("key1", "value1");
        dummyHeaderMap.put("key2", "value2");
        request.addHeaders(dummyHeaderMap);
        assertEquals(2L, request.getHeaders().size());
    }
}
