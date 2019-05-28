package com.github.scribejava.core.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.java8.Base64;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2Authorization;
import com.github.scribejava.core.model.OAuthConstants;
import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public class OAuth20ServiceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    @Test
    public void shouldProduceCorrectRequestSync() throws IOException, InterruptedException, ExecutionException {
        final OAuth20Service service = new ServiceBuilder("your_api_key")
                .apiSecret("your_api_secret")
                .build(new OAuth20ApiUnit());

        final OAuth2AccessToken token = service.getAccessTokenPasswordGrant("user1", "password1");
        assertNotNull(token);

        final JsonNode response = OBJECT_MAPPER.readTree(token.getRawResponse());

        assertEquals(OAuth20ServiceUnit.TOKEN, response.get(OAuthConstants.ACCESS_TOKEN).asText());
        assertEquals(OAuth20ServiceUnit.EXPIRES, response.get("expires_in").asInt());

        final String authorize = base64Encoder.encodeToString(
                String.format("%s:%s", service.getApiKey(), service.getApiSecret()).getBytes(Charset.forName("UTF-8")));

        assertEquals(OAuthConstants.BASIC + ' ' + authorize, response.get(OAuthConstants.HEADER).asText());

        assertEquals("user1", response.get("query-username").asText());
        assertEquals("password1", response.get("query-password").asText());
        assertEquals("password", response.get("query-grant_type").asText());
    }

    @Test
    public void shouldProduceCorrectRequestAsync() throws ExecutionException, InterruptedException, IOException {
        final OAuth20Service service = new ServiceBuilder("your_api_key")
                .apiSecret("your_api_secret")
                .build(new OAuth20ApiUnit());

        final OAuth2AccessToken token = service.getAccessTokenPasswordGrantAsync("user1", "password1").get();

        assertNotNull(token);

        final JsonNode response = OBJECT_MAPPER.readTree(token.getRawResponse());

        assertEquals(OAuth20ServiceUnit.TOKEN, response.get(OAuthConstants.ACCESS_TOKEN).asText());
        assertEquals(OAuth20ServiceUnit.EXPIRES, response.get("expires_in").asInt());

        final String authorize = base64Encoder.encodeToString(
                String.format("%s:%s", service.getApiKey(), service.getApiSecret()).getBytes(Charset.forName("UTF-8")));

        assertEquals(OAuthConstants.BASIC + ' ' + authorize, response.get(OAuthConstants.HEADER).asText());

        assertEquals("user1", response.get("query-username").asText());
        assertEquals("password1", response.get("query-password").asText());
        assertEquals("password", response.get("query-grant_type").asText());
    }

    @Test
    public void testOAuthExtractAuthorization() {
        final OAuth20Service service = new ServiceBuilder("your_api_key")
                .apiSecret("your_api_secret")
                .build(new OAuth20ApiUnit());

        OAuth2Authorization authorization = service.extractAuthorization("https://cl.ex.com/cb?code=SplxlOB&state=xyz");
        assertEquals("SplxlOB", authorization.getCode());
        assertEquals("xyz", authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?state=xyz&code=SplxlOB");
        assertEquals("SplxlOB", authorization.getCode());
        assertEquals("xyz", authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?key=value&state=xyz&code=SplxlOB");
        assertEquals("SplxlOB", authorization.getCode());
        assertEquals("xyz", authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?state=xyz&code=SplxlOB&key=value&");
        assertEquals("SplxlOB", authorization.getCode());
        assertEquals("xyz", authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?code=SplxlOB&state=");
        assertEquals("SplxlOB", authorization.getCode());
        assertEquals(null, authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?code=SplxlOB");
        assertEquals("SplxlOB", authorization.getCode());
        assertEquals(null, authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?code=");
        assertEquals(null, authorization.getCode());
        assertEquals(null, authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?code");
        assertEquals(null, authorization.getCode());
        assertEquals(null, authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb?");
        assertEquals(null, authorization.getCode());
        assertEquals(null, authorization.getState());

        authorization = service.extractAuthorization("https://cl.ex.com/cb");
        assertEquals(null, authorization.getCode());
        assertEquals(null, authorization.getState());
    }
}
