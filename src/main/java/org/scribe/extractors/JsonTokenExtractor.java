package org.scribe.extractors;

import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class JsonTokenExtractor implements AccessTokenExtractor
{
  private Pattern accessTokenPattern = Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");
  private Pattern tokenTypePattern = Pattern.compile("\"token_type\"\\s*:\\s*\"(\\S*?)\"");

  public Token extract(String response)
  {
    Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
    Matcher matcherAccessToken = accessTokenPattern.matcher(response);
    Matcher matcherTokenType = tokenTypePattern.matcher(response);
    if(matcherAccessToken.find())
    {
      if(matcherTokenType.find()) return new Token(matcherAccessToken.group(1), matcherTokenType.group(1), response);
      else return new Token(matcherAccessToken.group(1), "", response);
    }
    else
    {
      throw new OAuthException("Cannot extract an access token. Response was: " + response);
    }
  }

}