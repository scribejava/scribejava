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

	private final String token;
	private final String secret; // oauth 1.0: secret, oauth 2.0 refresh token
	private final long expiresAt;
	private long uid;
	private String userName;
	private final String rawResponse;

	// public OAuthToken(String rawResponse) {
	// this.rawResponse = rawResponse;
	// }

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

	public OAuthToken(String token, String secret, String rawResponse) {
		this(token, secret, 0L, rawResponse);
	}

	public OAuthToken(String token, String secret, long expiresAt,
			String rawResponse) {
		this(token, secret, expiresAt, 0L, null, rawResponse);
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

	public String getToken() {
		return token;
	}

	public String getSecret() {
		return secret;
	}

	public long getExpiresAt() {
		return expiresAt;
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
		if (rawResponse == null) {
			throw new IllegalStateException(
					"This token object was not constructed by scribe and does not have a rawResponse");
		}
		return rawResponse;
	}

	@Override
	public String toString() {
		return String.format("Token[%s , %s]", token, secret);
	}

	/**
	 * Returns true if the token is empty (token = "", secret = "")
	 */
	public boolean isEmpty() {
		return "".equals(this.token) && "".equals(this.secret);
	}

	/**
	 * Factory method that returns an empty token (token = "", secret = "").
	 * 
	 * Useful for two legged OAuth.
	 */
	public static OAuthToken empty() {
		return new OAuthToken("", "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (expiresAt ^ (expiresAt >>> 32));
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
		if (expiresAt != other.expiresAt)
			return false;
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
}
