package org.scribe.utils;

import java.util.regex.Pattern;

import org.scribe.model.OAuthConstants;

public class Preconditions
{
  private static final String DEFAULT_MESSAGE = "Received an invalid parameter";
  private static final Pattern URL_PATTERN = Pattern.compile("[a-zA-Z_-]+://\\S+");

  public static void checkNotNull(Object object, String errorMsg)
  {
    check(object != null, errorMsg);
  }

  public static void checkEmptyString(String string, String errorMsg)
  {
    check(string != null && !string.trim().equals(""), errorMsg);
  }

  public static void checkValidUrl(String url, String errorMsg)
  {
    checkEmptyString(url, errorMsg);
    check(isUrl(url), errorMsg);
  }
  
  public static void checkValidOAuthCallback(String url, String errorMsg)
  {
    checkEmptyString(url, errorMsg);
    if(url.toLowerCase().compareToIgnoreCase(OAuthConstants.OUT_OF_BAND) != 0)
    {
      check(isUrl(url), errorMsg);  
    }
  }
  
  static boolean isUrl(String url)
  {
    return URL_PATTERN.matcher(url).matches();
  }
  
  private static void check(boolean requirements, String error)
  {
    String message = (error == null || error.trim().length() <= 0) ? DEFAULT_MESSAGE : error;
    if (!requirements)
    {
      throw new IllegalArgumentException(message);
    }
  }
  
}
