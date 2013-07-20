package org.scribe.extractors;

import java.util.Date;
import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class JsonTokenExtractor implements AccessTokenExtractor
{
  private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");
  private static final Pattern expirePattern = Pattern.compile("\"expires_in\":\\s*(\\d+)");

  public Token extract(String response)
  {
    Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");

    Matcher matcher = accessTokenPattern.matcher(response);
    if(matcher.find())
    {
      Date expireDate = null;
      Matcher expireMatcher = expirePattern.matcher(response);
      if (expireMatcher.find())
      {
        long expiration = Long.parseLong(expireMatcher.group(1));
        expireDate = new Date(System.currentTimeMillis() + expiration * 1000);
      }
      return new Token(matcher.group(1), "", response, expireDate);
    }
    else
    {
      throw new OAuthException("Cannot extract an acces token. Response was: " + response);
    }
  }

}