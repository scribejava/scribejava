package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.apache.commons.codec.CharEncoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class OdnoklassnikiServiceImpl extends OAuth20Service {

    public OdnoklassnikiServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(OAuth2AccessToken accessToken, AbstractRequest request) {
        // sig = md5( request_params_composed_string+ md5(access_token + application_secret_key)  )
        try {
            final String tokenDigest = md5Hex(accessToken.getAccessToken() + getConfig().getApiSecret());

            final String completeUrl = request.getCompleteUrl();
            final int queryIndex = completeUrl.indexOf('?');
            if (queryIndex != -1) {
                String[] params = completeUrl.substring(queryIndex + 1).split("&");
                final String sigSource
                        = URLDecoder.decode(collectParams(params), CharEncoding.UTF_8)
                        + tokenDigest;
                request.addQuerystringParameter("sig", md5Hex(sigSource));
            }

            super.signRequest(accessToken, request);
        } catch (UnsupportedEncodingException unex) {
            throw new IllegalStateException(unex);
        }
    }

    private String collectParams(String[] queryParams){
        Map<String, String> paramsMap = new TreeMap<>();
        for(String param : queryParams) {
            String[] splited = param.split("=");
            if (splited.length != 0) {
                paramsMap.put(splited[0], param);
            }
        }
        return concatParams(paramsMap);
    }

    private String concatParams(Map mp) {
        StringBuilder builder = new StringBuilder();
        for (Object o : mp.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            builder.append(pair.getValue());
            builder.append(",");
        }
        return builder.toString();
    }
}
