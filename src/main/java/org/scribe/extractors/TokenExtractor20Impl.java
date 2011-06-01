package org.scribe.extractors;

import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Default implementation of {@AccessTokenExtractor}. Conforms to OAuth 2.0
 *
 */
public class TokenExtractor20Impl implements AccessTokenExtractor
{
  private static final String EMPTY_SECRET = "";

  private final Pattern tokenPattern;
  private final boolean urlEncoded;

  public TokenExtractor20Impl()
  {
    this("access_token=(\\S*?)(&(\\S*))?", true);
  }

  protected TokenExtractor20Impl(String tokenRegex, boolean urlEncoded)
  {
    tokenPattern = Pattern.compile(tokenRegex, Pattern.MULTILINE);
    this.urlEncoded = urlEncoded;
  }

  /**
   * {@inheritDoc} 
   */
  public Token extract(String response)
  {
    Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

    Matcher matcher = tokenPattern.matcher(response);
    if (matcher.matches())
    {
      String token = matcher.group(1);
      if (urlEncoded)
      {
        token = URLUtils.formURLDecode(token);
      }
      return new Token(token, EMPTY_SECRET, response);
    }
    else
    {
      throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
    }
  }
}
