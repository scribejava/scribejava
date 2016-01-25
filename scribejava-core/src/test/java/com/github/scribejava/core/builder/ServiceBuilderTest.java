package com.github.scribejava.core.builder;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.SignatureType;
import com.github.scribejava.core.oauth.OAuth20Service;

public class ServiceBuilderTest {

    private ServiceBuilder builder;
    private ApiMock api;

    @Before
    public void setUp() {
        builder = new ServiceBuilder();
        api = ApiMock.instance();
    }

    @Test
    public void shouldReturnConfigDefaultValues() {
        builder.apiKey("key").apiSecret("secret").build(api);

        final OAuthConfig config = api.getConfig();
        assertEquals(config.getApiKey(), "key");
        assertEquals(config.getApiSecret(), "secret");
        assertEquals(config.getCallback(), OAuthConstants.OUT_OF_BAND);
        assertEquals(config.getSignatureType(), SignatureType.Header);
    }

    @Test
    public void shouldAcceptValidCallbackUrl() {
        builder.apiKey("key").apiSecret("secret").callback("http://example.com").build(api);

        final OAuthConfig config = api.getConfig();
        assertEquals(config.getApiKey(), "key");
        assertEquals(config.getApiSecret(), "secret");
        assertEquals(config.getCallback(), "http://example.com");
    }

    @Test
    public void shouldAcceptASignatureType() {
        builder.apiKey("key").apiSecret("secret").signatureType(SignatureType.QueryString).build(api);

        final OAuthConfig config = api.getConfig();
        assertEquals(config.getApiKey(), "key");
        assertEquals(config.getApiSecret(), "secret");
        assertEquals(config.getSignatureType(), SignatureType.QueryString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptNullAsCallback() {
        builder.apiKey("key").apiSecret("secret").callback(null).build(api);
    }

    @Test
    public void shouldAcceptAnScope() {
        builder.apiKey("key").apiSecret("secret").scope("rss-api").build(api);

        final OAuthConfig config = api.getConfig();
        assertEquals(config.getApiKey(), "key");
        assertEquals(config.getApiSecret(), "secret");
        assertEquals(config.getScope(), "rss-api");
    }

    private static class ApiMock extends DefaultApi20 {

        private OAuthConfig config;

        private static ApiMock instance() {
            return new ApiMock();
        }

        private OAuthConfig getConfig() {
            return config;
        }

        @Override
        public OAuth20Service createService(OAuthConfig config) {
            this.config = config;
            return null;
        }

        @Override
        public String getAccessTokenEndpoint() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getAuthorizationUrl(OAuthConfig config) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
