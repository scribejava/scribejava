package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.core.utils.StreamUtils;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class OkHttpHttpClientTest {
    private OAuthService<?> oAuthService;

    @Before
    public void setUp() {
        final HttpClient client = new OkHttpHttpClient(new OkHttpClient());
        oAuthService = new OAuth20Service(null,
                new OAuthConfig("test", "test", null, null, null, null, null, null, null, client));
    }


    @Test
    public void shouldSendGetRequest() throws Exception {
        final String expectedResponseBody = "response body";

        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.start();

        final HttpUrl baseUrl = server.url("/testUrl");

        final OAuthRequest request = new OAuthRequest(Verb.GET, baseUrl.toString());
        final Response response = oAuthService.execute(request, null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, response.getBody());

        final RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

        server.shutdown();
    }

    @Test
    public void shouldSendPostRequest() throws Exception {
        final String expectedResponseBody = "response body";
        final String expectedRequestBody = "request body";

        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.start();

        final HttpUrl baseUrl = server.url("/testUrl");

        // request with body
        OAuthRequest request = new OAuthRequest(Verb.POST, baseUrl.toString());
        request.setPayload(expectedRequestBody);
        Response response = oAuthService.execute(request, null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, response.getBody());

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(expectedRequestBody, recordedRequest.getBody().readUtf8());


        // request with empty body
        request = new OAuthRequest(Verb.POST, baseUrl.toString());
        response = oAuthService.execute(request, null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, response.getBody());

        recordedRequest = server.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("", recordedRequest.getBody().readUtf8());

        server.shutdown();
    }

    @Test
    public void shouldReadResponseStream() throws Exception {
        final String expectedResponseBody = "response body";

        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.start();

        final HttpUrl baseUrl = server.url("/testUrl");

        final OAuthRequest request = new OAuthRequest(Verb.GET, baseUrl.toString());
        final Response response = oAuthService.execute(request, null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, StreamUtils.getStreamContents(response.getStream()));

        final RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

        server.shutdown();
    }
}
