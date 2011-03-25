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
  private static final String PAIR_SEPARATOR = "=";
  private static final String PARAM_SEPARATOR = "&";
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
   * Turns a map into a form-urlencoded string
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
      encodedString.append(PARAM_SEPARATOR).append(formURLEncode(key));
      if(map.get(key) != null)
      {
        encodedString.append(PAIR_SEPARATOR).append(formURLEncode(map.get(key)));
      }
    }
    return encodedString.toString().substring(1);
  }

  /**
   * Percent encodes a string
   * 
   * @param string plain string
   * @return percent encoded string
   */
  public static String percentEncode(String string)
  {
    String encoded = formURLEncode(string);
    for (EncodingRule rule : ENCODING_RULES)
    {
      encoded = rule.apply(encoded);
    }
    return encoded;
  }

  /**
   * Translates a string into application/x-www-form-urlencoded format
   *
   * @param plain
   * @return form-urlencoded string
   */
  public static String formURLEncode(String string)
  {
    Preconditions.checkNotNull(string, "Cannot encode null string");
    try
    {
      return URLEncoder.encode(string, UTF_8);
    } 
    catch (UnsupportedEncodingException uee)
    {
      throw new IllegalStateException(ERROR_MSG, uee);
    }
  }

  /**
   * Decodes a application/x-www-form-urlencoded string
   * 
   * @param string form-urlencoded string
   * @return plain string
   */
  public static String formURLDecode(String string)
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
    if (queryString.equals(EMPTY_STRING))
    {
      return url;
    }
    else
    {
      url += url.indexOf(QUERY_STRING_SEPARATOR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
      url += queryString;
      return url;
    }
  }

  /**
   * Concats a key-value map into a querystring-like String
   *
   * @param params key-value map
   * @return querystring-like String
   */
  // TODO Move to MapUtils
  public static String concatSortedPercentEncodedParams(Map<String, String> params)
  {
    StringBuilder result = new StringBuilder();
    for (String key : params.keySet())
    {
      result.append(key).append(PAIR_SEPARATOR);
      result.append(params.get(key)).append(PARAM_SEPARATOR);
    }
    return result.toString().substring(0, result.length() - 1);
  }

  /**
   * Parses and form-urldecodes a querystring-like string into a map
   *
   * @param queryString querystring-like String
   * @return a map with the form-urldecoded parameters
   */
  // TODO Move to MapUtils
  public static Map<String, String> queryStringToMap(String queryString)
  {
    Map<String, String> result = new HashMap<String, String>();
    if (queryString != null && queryString.length() > 0)
    {
      for (String param : queryString.split(PARAM_SEPARATOR))
      {
        String pair[] = param.split(PAIR_SEPARATOR);
        String key = formURLDecode(pair[0]);
        String value = pair.length > 1 ? formURLDecode(pair[1]) : EMPTY_STRING;
        result.put(key, value);
      }
    }
    return result;
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
