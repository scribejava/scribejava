package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.services.Base64Encoder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Map;

/**
 */
public class OAuth20ServiceTest {

    @Test
    public void shouldProduceCorrectRequest() {
        final OAuth20Service service = new ServiceBuilder()
            .apiKey("your_api_key")
            .apiSecret("your_api_secret")
            .build( new OAuth20ApiUnit() );

        final OAuth2AccessToken token = service.getAccessTokenPasswordGrant("user1", "password1");
        final Gson json = new Gson();

        Assert.assertNotNull(token);

        Map<String, String> map = json.fromJson(token.getRawResponse(),
            new TypeToken<Map<String, String>>(){}.getType());

        Assert.assertEquals(OAuth20ServiceUnit.token, map.get(OAuthConstants.ACCESS_TOKEN));
        Assert.assertEquals(OAuth20ServiceUnit.state, map.get(OAuthConstants.STATE));
        Assert.assertEquals(OAuth20ServiceUnit.expires, map.get("expires_in"));

        final String authorize = Base64Encoder.getInstance().encode(
            String.format("%s:%s", service.getConfig().getApiKey(), service.getConfig().getApiSecret()).getBytes(
                Charset.forName("UTF-8")
            )
        );

        Assert.assertEquals(OAuthConstants.BASIC + " " + authorize, map.get(OAuthConstants.HEADER));

        Assert.assertEquals("user1", map.get("query-username"));
        Assert.assertEquals("password1", map.get("query-password"));
        Assert.assertEquals("password", map.get("query-grant_type"));
    }
}
