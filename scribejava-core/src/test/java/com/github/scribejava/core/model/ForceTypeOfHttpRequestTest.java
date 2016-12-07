package com.github.scribejava.core.model;

import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;

public class ForceTypeOfHttpRequestTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private OAuthRequest request;
    private OAuthRequestAsync requestAsync;
    private OAuthService<?> oAuthService;

    @Before
    public void setUp() {
        ScribeJavaConfig.setForceTypeOfHttpRequests(ForceTypeOfHttpRequest.NONE);
        final OAuthConfig config = new OAuthConfig("test", "test");

        oAuthService = new OAuth20Service(null, config);
        request = new OAuthRequest(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces", config);
        requestAsync
                = new OAuthRequestAsync(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces");
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
        oAuthService.execute(requestAsync, null).get();
    }
}
