package org.scribe.model;

import java.io.Serializable;

import org.scribe.utils.Preconditions;

/**
 * Represents an OAuth token (either request or access token) and its secret
 * extended by mcxiaoke ,add expiresAt, uid, userName fields
 * 
 * @author Pablo Fernandez
 * @author mcxiaoke
 */
public class OAuthToken implements Serializable {
	private static final long serialVersionUID = 715000866082812683L;

	/**
	 * Factory method that returns an empty token (token = "", secret = "").
	 * 
	 * Useful for two legged OAuth.
	 */
	public static OAuthToken EMPTY() {
		return new OAuthToken("", "");
	}

	private String token;
	private String secret; // oauth 1.0: secret, oauth 2.0 refresh token
	private long expiresAt;
	private long uid;
	private String userName;
	private String rawResponse;

	public OAuthToken(OAuthToken token) {
		this(token.getToken(), token.getSecret(), token.getExpiresAt(), token
				.getUid(), token.getUserName(), token.getRawResponse());
	}

	public void update(OAuthToken token) {
		if (token != null && !token.isEmpty()) {
			this.token = token.getToken();
			this.secret = token.getSecret();
			this.expiresAt = token.getExpiresAt();
			this.uid = token.getUid();
			this.userName = token.getUserName();
			this.rawResponse = token.getRawResponse();
		}
	}

	public void update(String token, String secret) {
		if (isNotEmpty(token) && isNotEmpty(secret)) {
			this.token = token;
			this.secret = secret;
		}
	}

	/**
	 * Default constructor
	 * 
	 * @param token
	 *            token value. Can't be null.
	 * @param secret
	 *            token secret. Can't be null.
	 */
	public OAuthToken(String token, String secret) {
		this(token, secret, null);
	}

	public OAuthToken(String token, String secret, long expiresAt) {
		this(token, secret, expiresAt, 0L, null, null);
	}

	public OAuthToken(String token, String secret, long expiresAt, long uid) {
		this(token, secret, expiresAt, uid, null, null);
	}

	public OAuthToken(String token, String secret, long expiresAt, long uid,
			String rawResponse) {
		this(token, secret, expiresAt, uid, null, rawResponse);
	}

	public OAuthToken(String token, String secret, long expiresAt, long uid,
			String userName, String rawResponse) {
		Preconditions.checkNotNull(token, "Token can't be null");
		Preconditions.checkNotNull(secret, "Secret can't be null");
		this.token = token;
		this.secret = secret;
		this.expiresAt = expiresAt;
		this.uid = uid;
		this.userName = userName;
		this.rawResponse = rawResponse;
	}

	public OAuthToken(String token, String secret, long expiresAt,
			String rawResponse) {
		this(token, secret, expiresAt, 0L, null, rawResponse);
	}

	public OAuthToken(String token, String secret, String rawResponse) {
		this(token, secret, 0L, rawResponse);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	/**
	 * Returns true if the token is empty (token = "", secret = "")
	 */
	public boolean isEmpty() {
		return "".equals(this.token) && "".equals(this.secret);
	}

	private static boolean isNotEmpty(String text) {
		return text != null && text.length() > 0;
	}

	private static boolean isNullOrEmpty(String text) {
		return text == null && text.length() == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OAuthToken other = (OAuthToken) obj;
		if (secret == null) {
			if (other.secret != null)
				return false;
		} else if (!secret.equals(other.secret))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OAuthToken [token=");
		builder.append(token);
		builder.append(", secret=");
		builder.append(secret);
		builder.append(", expiresAt=");
		builder.append(expiresAt);
		builder.append(", uid=");
		builder.append(uid);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", rawResponse=");
		builder.append(rawResponse);
		builder.append("]");
		return builder.toString();
	}

}
