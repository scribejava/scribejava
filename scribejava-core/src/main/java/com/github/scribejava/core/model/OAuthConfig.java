package com.github.scribejava.core.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Parameter object that groups OAuth config values
 *
 * @author Pablo Fernandez
 */
public class OAuthConfig {

    private final String apiKey;
    private final String apiSecret;
    private final String callback;
    private final SignatureType signatureType;
    private final String scope;
    private final String grantType;
    private final OutputStream debugStream;
    private final Integer connectTimeout;
    private final Integer readTimeout;
    private String state;

    public OAuthConfig(final String key, final String secret) {
        this(key, secret, null, null, null, null, null, null, null);
    }

    public OAuthConfig(final String key, final String secret, final String callback, final SignatureType type,
            final String scope, final OutputStream stream, final Integer connectTimeout, final Integer readTimeout,
            final String grantType) {
        this.apiKey = key;
        this.apiSecret = secret;
        this.callback = callback;
        this.signatureType = type;
        this.scope = scope;
        this.debugStream = stream;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.grantType = grantType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getCallback() {
        return callback;
    }

    public SignatureType getSignatureType() {
        return signatureType;
    }

    public String getScope() {
        return scope;
    }

    public boolean hasScope() {
        return scope != null;
    }

    public String getGrantType() {
        return grantType;
    }

    public boolean hasGrantType() {
        return grantType != null;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void log(String message) {
        if (debugStream != null) {
            message += '\n';
            try {
                debugStream.write(message.getBytes("UTF8"));
            } catch (IOException | RuntimeException e) {
                throw new RuntimeException("there were problems while writting to the debug stream", e);
            }
        }
    }

    /**
     * Sets optional value used by some provider implementations that is exchanged with provider to avoid CSRF attacks.
     *
     * @param state some secret key that client side shall never receive
     */
    public void setState(final String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

}
