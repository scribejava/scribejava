package com.github.scribejava.core.model;

import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;
import com.github.scribejava.core.oauth.OAuthService;

public class ForceTypeOfHttpRequestTest {

    private ConnectionStub connection;
    private OAuthRequest request;
    private OAuthRequestAsync requestAsync;
    private OAuthService oAuthService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        connection = new ConnectionStub();
        ScribeJavaConfig.setForceTypeOfHttpRequests(ForceTypeOfHttpRequest.NONE);
        oAuthService = new OAuth20ServiceImpl(null, new OAuthConfig("test", "test"));
        request = new OAuthRequest(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces", oAuthService);
        requestAsync = new OAuthRequestAsync(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces", oAuthService);
    }

    @Test
    public void shouldNotSendSyncWithForceParameter() {
        expectedException.expect(OAuthException.class);
        expectedException.expectMessage("Cannot use sync operations, only async");
        ScribeJavaConfig.setForceTypeOfHttpRequests(ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS);
        request.send();
    }

    @Test
    public void shouldNotSendAsyncWithForceParameter() throws ExecutionException, InterruptedException {
        expectedException.expect(OAuthException.class);
        expectedException.expectMessage("Cannot use async operations, only sync");
        ScribeJavaConfig.setForceTypeOfHttpRequests(ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS);
        requestAsync.sendAsync(null).get();
    }
}
