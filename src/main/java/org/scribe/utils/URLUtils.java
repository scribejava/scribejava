package org.scribe.utils;

import java.io.*;
import java.net.*;
import java.util.*;

import org.scribe.exceptions.*;

/**
 * Utils to deal with URL and url-encodings 
 * 
 * @author Pablo Fernandez
 */
public class URLUtils
{
  private static final String EMPTY_STRING = "";
  private static final String UTF_8 = "UTF-8";
  private static final char PAIR_SEPARATOR = '=';
  private static final char PARAM_SEPARATOR = '&';

  private static final String ERROR_MSG = String.format("Cannot find specified encoding: %s", UTF_8);

  /**
   * Turns a map into a form-url-encoded string (key=value&key2=value2)
   * 
   * @param map any map
   * @return form-url-encoded string
   */
  public static String formURLEncodeMap(Map<String, String> map)
  {
    Preconditions.checkNotNull(map, "Cannot url-encode a null object");
    return (map.size() <= 0) ? EMPTY_STRING : doFormUrlEncode(map);
  }

  private static String doFormUrlEncode(Map<String, String> map)
  {
    StringBuffer encodedString = new StringBuffer(map.size() * 20);
    for (String key : map.keySet())
    {
      if(encodedString.length() > 0) 
      {
        encodedString.append(PARAM_SEPARATOR);
      }
      encodedString.append(percentEncode(key)).append(PAIR_SEPARATOR).append(percentEncode(map.get(key)));
    }
    return encodedString.toString();
  }

  /**
   * Percent encodes a string
   * 
   * @param plain
   * @return percent encoded string
   */
  public static String percentEncode(String string)
  {
    Preconditions.checkNotNull(string, "Cannot encode null string");
    try
    {
      return URLEncoder.encode(string, UTF_8).replaceAll("\\+", "%20");
    } 
    catch (UnsupportedEncodingException uee)
    {
      throw new OAuthException(ERROR_MSG, uee);
    }
  }

  /**
   * Percent decodes a string
   * 
   * @param string percent encoded string
   * @return plain string
   */
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
