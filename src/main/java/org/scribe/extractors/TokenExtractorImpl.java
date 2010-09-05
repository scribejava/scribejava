package org.scribe.extractors;

import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class TokenExtractorImpl implements RequestTokenExtractor, AccessTokenExtractor
{

  private static final String TOKEN_REGEX = "oauth_token=(\\S*)&oauth_token_secret=(\\S*?)(&(.*))?";

  public Token extract(String response)
  {
    Preconditions.checkEmptyString(response, "Cant extract a token from null object or an empty string.");

    Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
    if (matcher.matches())
    {
      String token = URLUtils.percentDecode(matcher.group(1));
      String secret = URLUtils.percentDecode(matcher.group(2));
      return new Token(token, secret);
    } else
    {
      throw new OAuthException("Could not find request token or secret in response: " + response, null);
    }
  }
}
