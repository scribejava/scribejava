package org.scribe.extractors;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;

/**
 * @author Boris G. Tsirkin <mail@dotbg.name>
 * @since 20.4.2011
 */
public class VkontakteTokenExtractor extends TokenExtractor20Impl {
  /**
   * {@inheritDoc}
   */
  public Token extract(String response) {
    Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");
    try {
      final JSONObject jsonObject = new JSONObject(response);
      return new Token((String) jsonObject.get("access_token"), "", response);
    } catch (JSONException e) {
      throw new OAuthException(String.format("Could not parse response as json.Got: %s", response));
    }
  }
}
