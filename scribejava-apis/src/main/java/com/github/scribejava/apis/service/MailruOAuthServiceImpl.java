package com.github.scribejava.apis.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.CharEncoding;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;

public class MailruOAuthServiceImpl extends OAuth20ServiceImpl {

    public MailruOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(final Token accessToken, final AbstractRequest request) {
        // sig = md5(params + secret_key)
        request.addQuerystringParameter("session_key", accessToken.getToken());
        request.addQuerystringParameter("app_id", getConfig().getApiKey());
        String completeUrl = request.getCompleteUrl();

        try {
            final String clientSecret = getConfig().getApiSecret();
            final int queryIndex = completeUrl.indexOf('?');
            if (queryIndex != -1) {
                String urlPart = completeUrl.substring(queryIndex + 1);
                Map<String, String> map = new TreeMap<>();
                for (String param : urlPart.split("&")) {
                    String[] parts = param.split("=");
                    map.put(parts[0], (parts.length == 1) ? "" : parts[1]);
                }
                StringBuilder urlNew = new StringBuilder();
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
    protected <T extends AbstractRequest> T createAccessTokenRequest(final Verifier verifier, T request) {
        super.createAccessTokenRequest(verifier, request);
        if (!getConfig().hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, "authorization_code");
        }
        return (T) request;
    }
}
