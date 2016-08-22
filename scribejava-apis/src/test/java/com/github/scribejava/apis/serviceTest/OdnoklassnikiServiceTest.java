package com.github.scribejava.apis.serviceTest;

import com.github.scribejava.apis.OdnoklassnikiApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by alex on 22.08.16.
 */
public class OdnoklassnikiServiceTest {

    private OAuth20Service service;
    private static final String PROTECTED_RESOURCE_URL
            = "https://api.ok.ru/api/users/getCurrentUser?format=JSON&application_key=publicKey";

    @Before
    public void setUp() {
        service = new ServiceBuilder()
                .apiKey("clientId")
                .apiSecret("clientSecret")
                .scope("wall,offline")
                .callback("http://your.site.com/callback")
                .build(OdnoklassnikiApi.instance());
    }


    @Test
    public void shouldThrowExceptionIfBaseStringIsNull() {
        final String clientId  = "clientId";
        final String publicKey = "publicKey";
        final String secretKey = "secretKey";
        OAuth2AccessToken accessToken = new OAuth2AccessToken("token");
        OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL, publicKey), service);
        service.signRequest(accessToken, request);
        System.out.println(request.getBodyContents());
    }

}
