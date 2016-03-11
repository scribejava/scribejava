package com.github.scribejava.apis.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.CharEncoding;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;

public class MailruOAuthServiceImpl extends OAuth20Service {

    public MailruOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(OAuth2AccessToken accessToken, AbstractRequest request) {
        // sig = md5(params + secret_key)
        request.addQuerystringParameter("session_key", accessToken.getAccessToken());
        request.addQuerystringParameter("app_id", getConfig().getApiKey());
        final String completeUrl = request.getCompleteUrl();

        try {
            final String clientSecret = getConfig().getApiSecret();
            final int queryIndex = completeUrl.indexOf('?');
            if (queryIndex != -1) {
                final String urlPart = completeUrl.substring(queryIndex + 1);
                final Map<String, String> map = new TreeMap<>();
                for (String param : urlPart.split("&")) {
                    final String[] parts = param.split("=");
                    map.put(parts[0], (parts.length == 1) ? "" : parts[1]);
                }
                final StringBuilder urlNew = new StringBuilder();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    urlNew.append(entry.getKey());
                    urlNew.append('=');
                    urlNew.append(entry.getValue());
                }
                final String sigSource = URLDecoder.decode(urlNew.toString(), CharEncoding.UTF_8) + clientSecret;
                request.addQuerystringParameter("sig", md5Hex(sigSource));
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected <T extends AbstractRequest> T createAccessTokenRequest(String code, T request) {
        super.createAccessTokenRequest(code, request);
        if (!getConfig().hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
        }
        return request;
    }
}
