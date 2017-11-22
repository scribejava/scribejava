package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class FacebookService extends OAuth20Service {

    public FacebookService(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);

        final Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            final SecretKeySpec secretKey = new SecretKeySpec(getConfig().getApiSecret().getBytes(), "HmacSHA256");
            mac.init(secretKey);

            final Formatter appsecretProof = new Formatter();

            for (byte b : mac.doFinal(accessToken.getBytes())) {
                appsecretProof.format("%02x", b);
            }

            request.addParameter("appsecret_proof", appsecretProof.toString());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("There is a problem while generating Facebook appsecret_proof.", e);
        }
    }
}
