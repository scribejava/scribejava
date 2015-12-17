package com.github.scribejava.core.builder;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.builder.api.Api;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.SignatureType;
import com.github.scribejava.core.oauth.OAuthService;

public class ServiceBuilderTest {

    private ServiceBuilder builder;

    @Before
    public void setup() {
        builder = new ServiceBuilder();
    }

    @Test
    public void shouldReturnConfigDefaultValues() {
        builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").build();
        assertEquals(ApiMock.config.getApiKey(), "key");
        assertEquals(ApiMock.config.getApiSecret(), "secret");
        assertEquals(ApiMock.config.getCallback(), OAuthConstants.OUT_OF_BAND);
        assertEquals(ApiMock.config.getSignatureType(), SignatureType.Header);
    }

    @Test
    public void shouldAcceptValidCallbackUrl() {
        builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").callback("http://example.com").build();
        assertEquals(ApiMock.config.getApiKey(), "key");
        assertEquals(ApiMock.config.getApiSecret(), "secret");
        assertEquals(ApiMock.config.getCallback(), "http://example.com");
    }

    @Test
    public void shouldAcceptASignatureType() {
        builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").signatureType(SignatureType.QueryString).build();
        assertEquals(ApiMock.config.getApiKey(), "key");
        assertEquals(ApiMock.config.getApiSecret(), "secret");
        assertEquals(ApiMock.config.getSignatureType(), SignatureType.QueryString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptNullAsCallback() {
        builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").callback(null).build();
    }

    @Test
    public void shouldAcceptAnScope() {
        builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").scope("rss-api").build();
        assertEquals(ApiMock.config.getApiKey(), "key");
        assertEquals(ApiMock.config.getApiSecret(), "secret");
        assertEquals(ApiMock.config.getScope(), "rss-api");
    }

    public static class ApiMock implements Api {

        public static OAuthConfig config;

        public OAuthService createService(OAuthConfig config) {
            ApiMock.config = config;
            return null;
        }
    }
}
