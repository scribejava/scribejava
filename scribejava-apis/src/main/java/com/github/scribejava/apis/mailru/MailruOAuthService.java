package com.github.scribejava.apis.mailru;

import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class MailruOAuthService extends OAuth20Service {

    public MailruOAuthService(DefaultApi20 api, String apiKey, String apiSecret, String callback, String scope,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }


    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        // sig = md5(params + secret_key)
        request.addQuerystringParameter("session_key", accessToken);
        request.addQuerystringParameter("app_id", getApiKey());
        final String completeUrl = request.getCompleteUrl();

        try {
            final String clientSecret = getApiSecret();
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

                final String sigSource = URLDecoder.decode(urlNew.toString(), "UTF-8") + clientSecret;
                request.addQuerystringParameter("sig", md5(sigSource));
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String md5(String orgString) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(orgString.getBytes(Charset.forName("UTF-8")));
            final Formatter builder = new Formatter();
            for (byte b : array) {
                builder.format("%02x", b);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 is unsupported?", e);
        }
    }
}
