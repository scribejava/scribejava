package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Parameter;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.apache.commons.codec.CharEncoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class OdnoklassnikiServiceImpl extends OAuth20Service {

    public OdnoklassnikiServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(OAuth2AccessToken accessToken, OAuthRequest request) {
        //sig = lower(md5( sorted_request_params_composed_string + md5(access_token + application_secret_key)))
        try {
            final String tokenDigest = md5Hex(accessToken.getAccessToken() + getConfig().getApiSecret());

            final ParameterList queryParams = request.getQueryStringParams();
            queryParams.addAll(request.getBodyParams());
            final List<Parameter> allParams = queryParams.getParams();

            Collections.sort(allParams);

            final String stringParams = allParams.stream()
                    .map(param -> param.getKey() + '=' + param.getValue())
                    .collect(Collectors.joining());

            final String sigSource = URLDecoder.decode(stringParams, CharEncoding.UTF_8) + tokenDigest;
            request.addQuerystringParameter("sig", md5Hex(sigSource).toLowerCase());

            super.signRequest(accessToken, request);
        } catch (UnsupportedEncodingException unex) {
            throw new IllegalStateException(unex);
        }
    }
}
