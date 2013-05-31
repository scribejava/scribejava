package org.scribe.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class RefreshTokenExtractor20Impl implements RefreshTokenExtractor {

    private Pattern refreshTokenPattern = Pattern.compile("\""+ OAuthConstants.REFRESH_TOKEN + "\"\\s*:\\s*\"(\\S*?)\"");

    public Token extract(String response)
    {
      Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
      Matcher matcher = refreshTokenPattern.matcher(response);
      if(matcher.find())
      {
        return new Token(matcher.group(1), "", response);
      }
      else
      {
        throw new OAuthException("Cannot extract an acces token. Response was: " + response);
      }
    }

  }
