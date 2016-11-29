package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequestAsync;
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

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class OkHttpHttpClientTest {
    private OAuthService<?> oAuthService;

    @Before
    public void setUp() throws MalformedURLException {
        HttpClient client = new OkHttpHttpClient(new OkHttpClient());
        oAuthService = new OAuth20Service(null,
                new OAuthConfig("test", "test", null, null, null, null, null, null, null, null, null, null, client));
    }


    @Test
    public void shouldSendGetRequest() throws Exception {
        String expectedResponseBody = "response body";

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.start();

        HttpUrl baseUrl = server.url("/testUrl");

        OAuthRequestAsync request = new OAuthRequestAsync(Verb.GET, baseUrl.toString(), oAuthService);
        Response response = request.sendAsync(null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, response.getBody());

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

        server.shutdown();
    }

    @Test
    public void shouldSendPostRequest() throws Exception {
        String expectedResponseBody = "response body";
        String expectedRequestBody = "request body";

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.start();

        HttpUrl baseUrl = server.url("/testUrl");

        // request with body
        OAuthRequestAsync request = new OAuthRequestAsync(Verb.POST, baseUrl.toString(), oAuthService);
        request.addPayload(expectedRequestBody);
        Response response = request.sendAsync(null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, response.getBody());

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(expectedRequestBody, recordedRequest.getBody().readUtf8());


        // request with empty body
        request = new OAuthRequestAsync(Verb.POST, baseUrl.toString(), oAuthService);
        response = request.sendAsync(null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, response.getBody());

        recordedRequest = server.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("", recordedRequest.getBody().readUtf8());

        server.shutdown();
    }

    @Test
    public void shouldReadResponseStream() throws Exception {
        String expectedResponseBody = "response body";

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedResponseBody));
        server.start();

        HttpUrl baseUrl = server.url("/testUrl");

        OAuthRequestAsync request = new OAuthRequestAsync(Verb.GET, baseUrl.toString(), oAuthService);
        Response response = request.sendAsync(null).get(30, TimeUnit.SECONDS);

        assertEquals(expectedResponseBody, StreamUtils.getStreamContents(response.getStream()));

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

        server.shutdown();
    }
}
