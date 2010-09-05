package org.scribe.utils;

import java.io.*;
import java.net.*;
import java.util.*;

import org.scribe.exceptions.*;

public class URLUtils
{

  private static final String EMPTY_STRING = "";
  private static final String UTF_8 = "UTF-8";
  private static final char PAIR_SEPARATOR = '=';
  private static final char PARAM_SEPARATOR = '&';

  private static final String ERROR_MSG = String.format("Cannot find specified encoding: %s", UTF_8);

  public static String formURLEncodeMap(Map<String, String> map)
  {
    Preconditions.checkNotNull(map, "Cannot url-encode a null object");
    return (map.size() <= 0) ? EMPTY_STRING : doFormUrlEncode(map);
  }

  private static String doFormUrlEncode(Map<String, String> map)
  {
    StringBuffer encodedString = new StringBuffer();
    for (String key : map.keySet())
    {
      encodedString.append(percentEncode(key)).append(PAIR_SEPARATOR).append(percentEncode(map.get(key))).append(PARAM_SEPARATOR);
    }
    return removeTrailingSeparator(encodedString);
  }

  private static String removeTrailingSeparator(StringBuffer buffer)
  {
    return buffer.toString().substring(0, buffer.length() - 1);
  }

  public static String percentEncode(String string)
  {
    Preconditions.checkNotNull(string, "Cannot encode null string");
    try
    {
      return URLEncoder.encode(string, UTF_8);
    } catch (UnsupportedEncodingException uee)
    {
      throw new OAuthException(ERROR_MSG, uee);
    }
  }

  public static String percentDecode(String string)
  {
    Preconditions.checkNotNull(string, "Cannot decode null string");
    try
    {
      return URLDecoder.decode(string, UTF_8);
    } catch (UnsupportedEncodingException uee)
    {
      throw new OAuthException(ERROR_MSG, uee);
    }
  }
}
