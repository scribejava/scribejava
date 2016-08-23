package com.github.scribejava.apis.serviceTest;

import com.github.scribejava.apis.OdnoklassnikiApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by alex on 22.08.16.
 */
public class OdnoklassnikiServiceTest {

    private OAuth20Service service;
    private static final String URL
            = "https://api.ok.ru/fb.do?method=friends.get&fields=uid%2Cfirst_name%2Clast_name%2Cpic_2&application_key=AAAAAAAAAAAAAAAA&format=json";

    @Before
    public void setUp() {
        service = new ServiceBuilder()
                .apiKey("0000000000")
                .apiSecret("CCCCCCCCCCCCCCCCCCCCCCCC")
                .scope("VALUABLE_ACCESS")
                .callback("http://your.site.com/callback")
                .build(OdnoklassnikiApi.instance());
    }

    @Test
    public void shouldThrowExceptionIfBaseStringIsNull() {
        OAuth2AccessToken accessToken = new OAuth2AccessToken("d3iwa.403gvrs194740652m1k4w2a503k3c");
        OAuthRequest request = new OAuthRequest(Verb.GET, URL, service);
        service.signRequest(accessToken, request);

        //session_secret_key = md5Hex(accessToken.getAccessToken() + service.getConfig().getApiSecret()) == 129ea5bb368dfb321dc90f63da01b742
        //значения_параметров  == application_key=AAAAAAAAAAAAAAAAfields=uid%2Cfirst_name%2Clast_name%2Cpic_2format=jsonmethod=friends.get
        //sig == MD5(URLDecoder.decode(значения_параметров, CharEncoding.UTF_8) + session_secret_key) == 96127f5ca29a8351399e94bbd284ab16

        Assert.assertEquals(findParam(request.getQueryStringParams(), "sig"), "96127f5ca29a8351399e94bbd284ab16");
    }

    private String findParam(ParameterList list, String key){
        String value = "";
        for (Parameter param  : list.getParams()){
            if (param.getKey().equals(key)){
                value = param.getValue();
            }
        }
        return value;
    }

}
