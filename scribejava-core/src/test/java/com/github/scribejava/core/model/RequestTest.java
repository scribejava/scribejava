package com.github.scribejava.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;

public class RequestTest {

    private OAuthRequest getRequest;
    private OAuthRequest postRequest;

    @Before
    public void setUp() throws MalformedURLException {
        postRequest = new OAuthRequest(Verb.POST, "http://example.com");
        postRequest.addBodyParameter("param", "value");
        postRequest.addBodyParameter("param with spaces", "value with spaces");

        getRequest = new OAuthRequest(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces");
    }

    @Test
    public void shouldGetQueryStringParameters() {
        assertEquals(2, getRequest.getQueryStringParams().size());
        assertEquals(0, postRequest.getQueryStringParams().size());
        assertTrue(getRequest.getQueryStringParams().contains(new Parameter("qsparam", "value")));
    }

    @Test
    public void shouldSetBodyParamsAndAddContentLength() {
        assertEquals("param=value&param%20with%20spaces=value%20with%20spaces",
                new String(postRequest.getByteArrayPayload()));
    }

    @Test
    public void shouldSetPayloadAndHeaders() {
        postRequest.setPayload("PAYLOAD");
        assertEquals("PAYLOAD", postRequest.getStringPayload());
    }

    @Test
    public void shouldAllowAddingQuerystringParametersAfterCreation() {
        final OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com?one=val");
        request.addQuerystringParameter("two", "other val");
        request.addQuerystringParameter("more", "params");
        assertEquals(3, request.getQueryStringParams().size());
    }

    @Test
    public void shouldReturnTheCompleteUrl() {
        final OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com?one=val");
        request.addQuerystringParameter("two", "other val");
        request.addQuerystringParameter("more", "params");
        assertEquals("http://example.com?one=val&two=other%20val&more=params", request.getCompleteUrl());
    }

    @Test
    public void shouldHandleQueryStringSpaceEncodingProperly() {
        assertTrue(getRequest.getQueryStringParams().contains(new Parameter("other param", "value with spaces")));
    }
}
