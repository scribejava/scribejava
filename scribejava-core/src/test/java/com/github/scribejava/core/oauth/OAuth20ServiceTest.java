package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.java8.Base64;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2Authorization;
import com.github.scribejava.core.model.OAuthConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class OAuth20ServiceTest {

    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    @Test
    public void shouldProduceCorrectRequestSync() throws IOException, InterruptedException, ExecutionException {
        final OAuth20Service service = new ServiceBuilder("your_api_key")
                .apiSecret("your_api_secret")
                .build(new OAuth20ApiUnit());

        final OAuth2AccessToken token = service.getAccessTokenPasswordGrant("user1", "password1");
        final Gson json = new Gson();

        assertNotNull(token);

        final Map<String, String> map = json.fromJson(token.getRawResponse(), new TypeTokenImpl().getType());

        assertEquals(OAuth20ServiceUnit.TOKEN, map.get(OAuthConstants.ACCESS_TOKEN));
        assertEquals(OAuth20ServiceUnit.STATE, map.get(OAuthConstants.STATE));
        assertEquals(OAuth20ServiceUnit.EXPIRES, map.get("expires_in"));

        final String authorize = base64Encoder.encodeToString(
                String.format("%s:%s", service.getApiKey(), service.getApiSecret()).getBytes(Charset.forName("UTF-8")));

        assertEquals(OAuthConstants.BASIC + " " + authorize, map.get(OAuthConstants.HEADER));

        assertEquals("user1", map.get("query-username"));
        assertEquals("password1", map.get("query-password"));
        assertEquals("password", map.get("query-grant_type"));
    }

    @Test
    public void shouldProduceCorrectRequestAsync() throws ExecutionException, InterruptedException {
        final OAuth20Service service = new ServiceBuilder("your_api_key")
                .apiSecret("your_api_secret")
                .build(new OAuth20ApiUnit());

        final OAuth2AccessToken token = service.getAccessTokenPasswordGrantAsync("user1", "password1", null).get();
        final Gson json = new Gson();

        assertNotNull(token);

        final Map<String, String> map = json.fromJson(token.getRawResponse(), new TypeTokenImpl().getType());

        assertEquals(OAuth20ServiceUnit.TOKEN, map.get(OAuthConstants.ACCESS_TOKEN));
        assertEquals(OAuth20ServiceUnit.STATE, map.get(OAuthConstants.STATE));
        assertEquals(OAuth20ServiceUnit.EXPIRES, map.get("expires_in"));

        final String authorize = base64Encoder.encodeToString(
                String.format("%s:%s", service.getApiKey(), service.getApiSecret()).getBytes(Charset.forName("UTF-8")));

        assertEquals(OAuthConstants.BASIC + " " + authorize, map.get(OAuthConstants.HEADER));

        assertEquals("user1", map.get("query-username"));
        assertEquals("password1", map.get("query-password"));
        assertEquals("password", map.get("query-grant_type"));
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

    private static class TypeTokenImpl extends TypeToken<Map<String, String>> {

        private TypeTokenImpl() {
        }
    }

}
