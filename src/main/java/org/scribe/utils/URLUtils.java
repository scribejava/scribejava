package org.scribe.utils;

import java.io.*;
import java.net.*;
import java.util.*;

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
  private static final char QUERY_STRING_SEPARATOR = '?';

  private static final String ERROR_MSG = String.format("Cannot find specified encoding: %s", UTF_8);

  private static final Set<EncodingRule> ENCODING_RULES;

  static
  {
    Set<EncodingRule> rules = new HashSet<EncodingRule>();
    rules.add(new EncodingRule("*","%2A"));
    rules.add(new EncodingRule("+","%20"));
    rules.add(new EncodingRule("%7E", "~"));
    ENCODING_RULES = Collections.unmodifiableSet(rules);
  }

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
      String encoded = URLEncoder.encode(string, UTF_8);
      for(EncodingRule rule : ENCODING_RULES)
      {
        encoded = rule.apply(encoded);
      }
      return encoded;
    } 
    catch (UnsupportedEncodingException uee)
    {
      throw new IllegalStateException(ERROR_MSG, uee);
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
    }
    catch (UnsupportedEncodingException uee)
    {
      throw new IllegalStateException(ERROR_MSG, uee);
    }
  }

  /**
   * Append given parameters to the query string of the url
   *
   * @param url the url to append parameters to
   * @param params any map
   * @return new url with parameters on query string
   */
  public static String appendParametersToQueryString(String url, Map<String, String> params)
  {
    Preconditions.checkNotNull(url, "Cannot append to null URL");
    String queryString = URLUtils.formURLEncodeMap(params);
    if (queryString.isEmpty()) return url;

    // Check if there are parameters in the url already and use '&' instead of '?'
    url += url.indexOf(QUERY_STRING_SEPARATOR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
    url += queryString;
    return url;
  }

  private static final class EncodingRule
  {
    private final String ch;
    private final String toCh;

    EncodingRule(String ch, String toCh)
    {
      this.ch = ch;
      this.toCh = toCh;
    }

    String apply(String string) {
      return string.replace(ch, toCh);
    }
  }
}
