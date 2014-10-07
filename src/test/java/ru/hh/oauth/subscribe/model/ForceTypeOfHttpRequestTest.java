package ru.hh.oauth.subscribe.model;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.scribe.builder.api.FacebookApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.ConnectionStub;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.OAuthRequestAsync;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;

import java.util.concurrent.ExecutionException;

public class ForceTypeOfHttpRequestTest {

    private ConnectionStub connection;
    private OAuthRequest getRequest;
    private OAuthRequestAsync getRequestAsync;
    private OAuthService oAuthService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        connection = new ConnectionStub();
        oAuthService = new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig("test", "test"));
        getRequest = new OAuthRequest(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces", oAuthService);
        getRequestAsync = new OAuthRequestAsync(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces", oAuthService);
    }
    
    @Test
    public void shouldNotSendSyncWithForceParameter(){
        expectedException.expect(OAuthException.class);
        expectedException.expectMessage("Cannot use sync operations, only async");
        SubScribeConfig.setForceTypeRequest(ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS);
        getRequest.send();
    }

    @Test
    public void shouldNotSendAsyncWithForceParameter() throws ExecutionException, InterruptedException {
        expectedException.expect(OAuthException.class);
        expectedException.expectMessage("Cannot use async operations, only sync");
        SubScribeConfig.setForceTypeRequest(ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS);
        getRequestAsync.sendAsync(null).get();
    }
}
