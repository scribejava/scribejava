package com.github.scribejava.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;
import com.github.scribejava.core.oauth.OAuthService;

public class RequestTest {

    private OAuthRequest getRequest;
    private OAuthRequest postRequest;
    private ConnectionStub connection;
    private OAuthService oAuthService;

    @Before
    public void setup() throws Exception {
        connection = new ConnectionStub();
        oAuthService = new OAuth20ServiceImpl(null, new OAuthConfig("test", "test"));
        postRequest = new OAuthRequest(Verb.POST, "http://example.com", oAuthService);
        postRequest.addBodyParameter("param", "value");
        postRequest.addBodyParameter("param with spaces", "value with spaces");
        postRequest.setConnection(connection);
        getRequest = new OAuthRequest(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces", oAuthService);
        getRequest.setConnection(connection);
    }

    @Test
    public void shouldSetRequestVerb() {
        getRequest.send();
        assertEquals("GET", connection.getRequestMethod());
    }

    @Test
    public void shouldGetQueryStringParameters() {
        assertEquals(2, getRequest.getQueryStringParams().size());
        assertEquals(0, postRequest.getQueryStringParams().size());
        assertTrue(getRequest.getQueryStringParams().contains(new Parameter("qsparam", "value")));
    }

    @Test
    public void shouldAddRequestHeaders() {
        getRequest.addHeader("Header", "1");
        getRequest.addHeader("Header2", "2");
        getRequest.send();
        assertEquals(2, getRequest.getHeaders().size());
        assertEquals(2, connection.getHeaders().size());
    }

    @Test
    public void shouldSetBodyParamsAndAddContentLength() {
        assertEquals("param=value&param%20with%20spaces=value%20with%20spaces", postRequest.getBodyContents());
        postRequest.send();
        assertTrue(connection.getHeaders().containsKey("Content-Length"));
    }

    @Test
    public void shouldSetPayloadAndHeaders() {
        postRequest.addPayload("PAYLOAD");
        postRequest.send();
        assertEquals("PAYLOAD", postRequest.getBodyContents());
        assertTrue(connection.getHeaders().containsKey("Content-Length"));
    }

    @Test
    public void shouldAllowAddingQuerystringParametersAfterCreation() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com?one=val", oAuthService);
        request.addQuerystringParameter("two", "other val");
        request.addQuerystringParameter("more", "params");
        assertEquals(3, request.getQueryStringParams().size());
    }

    @Test
    public void shouldReturnTheCompleteUrl() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com?one=val", oAuthService);
        request.addQuerystringParameter("two", "other val");
        request.addQuerystringParameter("more", "params");
        assertEquals("http://example.com?one=val&two=other%20val&more=params", request.getCompleteUrl());
    }

    @Test
    public void shouldHandleQueryStringSpaceEncodingProperly() {
        assertTrue(getRequest.getQueryStringParams().contains(new Parameter("other param", "value with spaces")));
    }

    @Test
    public void shouldAutomaticallyAddContentTypeForPostRequestsWithBytePayload() {
        postRequest.addPayload("PAYLOAD".getBytes());
        postRequest.send();
        assertEquals(OAuthRequest.DEFAULT_CONTENT_TYPE, connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldAutomaticallyAddContentTypeForPostRequestsWithStringPayload() {
        postRequest.addPayload("PAYLOAD");
        postRequest.send();
        assertEquals(OAuthRequest.DEFAULT_CONTENT_TYPE, connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldAutomaticallyAddContentTypeForPostRequestsWithBodyParameters() {
        postRequest.send();
        assertEquals(OAuthRequest.DEFAULT_CONTENT_TYPE, connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldBeAbleToOverrideItsContentType() {
        postRequest.addHeader("Content-Type", "my-content-type");
        postRequest.send();
        assertEquals("my-content-type", connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldNotAddContentTypeForGetRequests() {
        getRequest.send();
        assertFalse(connection.getHeaders().containsKey("Content-Type"));
    }
}
