package com.github.scribejava.apis.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.commons.codec.CharEncoding;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;

public class OdnoklassnikiServiceImpl extends OAuth20ServiceImpl {

    public OdnoklassnikiServiceImpl(final DefaultApi20 api, final OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(final Token accessToken, final AbstractRequest request) {
        // sig = md5( request_params_composed_string+ md5(access_token + application_secret_key)  )
        try {
            final String tokenDigest = md5Hex((accessToken.getToken() + getConfig().getApiSecret()));

            final String completeUrl = request.getCompleteUrl();
            final int queryIndex = completeUrl.indexOf('?');
            if (queryIndex != -1) {
                final String sigSource = URLDecoder.decode(completeUrl.substring(queryIndex + 1).replace("&", ""), CharEncoding.UTF_8) + tokenDigest;
                request.addQuerystringParameter("sig", md5Hex(sigSource));
            }

            super.signRequest(accessToken, request);
        } catch (UnsupportedEncodingException unex) {
            throw new IllegalStateException(unex);
        }
    }
}
