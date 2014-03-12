package org.scribe.model;

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

    public OAuthConfig(String key, String secret) {
        this(key, secret, null, null, null, null, null, null, null);
    }

    public OAuthConfig(
            String key, String secret, String callback, SignatureType type, String scope, OutputStream stream,
            Integer connectTimeout, Integer readTimeout,
            String grantType) {
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
            message = message + "\n";
            try {
                debugStream.write(message.getBytes("UTF8"));
            } catch (Exception e) {
                throw new RuntimeException("there were problems while writting to the debug stream", e);
            }
        }
    }
}
