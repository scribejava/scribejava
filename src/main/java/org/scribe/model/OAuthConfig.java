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
	private final GrantType grantType;
	private final ResponseType responseType;
	private final SignatureType signatureType;
	private final String scope;
	private final OutputStream debugStream;

	public OAuthConfig(String key, String secret) {
		this(key, secret, null, null, null, null, null, null);
	}

	public OAuthConfig(String key, String secret, String callback,
			GrantType grantType, ResponseType responseType, SignatureType type,
			String scope, OutputStream stream) {
		this.apiKey = key;
		this.apiSecret = secret;
		this.callback = callback;
		this.grantType = grantType;
		this.responseType = responseType;
		this.signatureType = type;
		this.scope = scope;
		this.debugStream = stream;
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

	public GrantType getGrantType() {
		return grantType;
	}

	public ResponseType getResponseType() {
		return responseType;
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

	public void log(String message) {
		if (debugStream != null) {
			message = message + "\n";
			try {
				debugStream.write(message.getBytes("UTF8"));
			} catch (Exception e) {
				throw new RuntimeException(
						"there were problems while writting to the debug stream",
						e);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OAuthConfig [apiKey=");
		builder.append(apiKey);
		builder.append(", apiSecret=");
		builder.append(apiSecret);
		builder.append(", callback=");
		builder.append(callback);
		builder.append(", grantType=");
		builder.append(grantType);
		builder.append(", responseType=");
		builder.append(responseType);
		builder.append(", signatureType=");
		builder.append(signatureType);
		builder.append(", scope=");
		builder.append(scope);
		builder.append("]");
		return builder.toString();
	}
}
