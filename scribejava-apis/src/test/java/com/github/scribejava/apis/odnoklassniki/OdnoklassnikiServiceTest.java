package com.github.scribejava.apis.odnoklassniki;

import com.github.scribejava.apis.OdnoklassnikiApi;
import com.github.scribejava.core.builder.ServiceBuilder;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Parameter;
import com.github.scribejava.core.oauth.OAuth20Service;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OdnoklassnikiServiceTest {

    private static final String URL = "https://api.ok.ru/fb.do?method=friends.get&fields=uid%2C"
            + "first_name%2Clast_name%2Cpic_2&application_key=AAAAAAAAAAAAAAAA&format=json";

    private final OAuth20Service service = new ServiceBuilder("0000000000")
            .apiSecret("CCCCCCCCCCCCCCCCCCCCCCCC")
            .scope("VALUABLE_ACCESS")
            .callback("http://your.site.com/callback")
            .build(OdnoklassnikiApi.instance());

    @Test
    public void testSigGeneration() {
        final OAuth2AccessToken accessToken = new OAuth2AccessToken("d3iwa.403gvrs194740652m1k4w2a503k3c");
        final OAuthRequest request = new OAuthRequest(Verb.GET, URL);
        service.signRequest(accessToken, request);
        assertEquals("96127f5ca29a8351399e94bbd284ab16", findParam(request.getQueryStringParams(), "sig"));
    }

    private static String findParam(ParameterList list, String key) {
        for (Parameter param : list.getParams()) {
            if (param.getKey().equals(key)) {
                return param.getValue();
            }
        }
        return null;
    }
}
