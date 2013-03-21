/**
 * 
 */
package org.scribe.extractors;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthToken;
import org.scribe.utils.Preconditions;

/**
 * Douban JSON AccessToken Extractor
 */
// example response
// {
// "access_token":"a14afef0f66fcffce3e0fcd2e34f6ff4",
// "expires_in":3920,
// "refresh_token":"5d633d136b6d56a41829b73a424803ec",
// "douban_user_id":"1221"
// }
//
// {
// msg: "required_parameter_is_missing: client_id",
// code: 113,
// request: "GET /service/auth2/token"
// }
public final class DoubanTokenExtractor implements AccessTokenExtractor {

	@Override
	public OAuthToken extract(String response) {
		Preconditions.checkEmptyString(response,
				"Cannot extract a token from a null or empty String");
		String rawResponse = response;
		try {
			JSONObject json = new JSONObject(rawResponse);
			if (json.has("access_token")) {
				String accessToken = json.getString("access_token");
				String refreshToken = json.getString("refresh_token");
				long expiresAt = json.getLong("expires_in")
						+ System.currentTimeMillis();
				long uid = json.getLong("douban_user_id");
				return new OAuthToken(accessToken, refreshToken, expiresAt,
						uid, rawResponse);
			} else {
				if (json.has("code")) {
					int errorCode = json.getInt("code");
					String errorMessage = json.getString("msg");
					String errorRequest = json.getString("request");
				}
				throw new OAuthException(
						"Cannot extract an acces token. Response was: "
								+ response);
			}
		} catch (JSONException e) {
			throw new OAuthException(
					"Cannot extract an acces token. Response was: " + response);
		}
	}

}
