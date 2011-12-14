package org.scribe.utils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import org.scribe.exceptions.*;

/**
 * @author: Pablo Fernandez
 */
public class OAuthEncoder
{
  private static String CHARSET = "UTF-8";
  private static final Map<String, String> ENCODING_RULES;

  static
  {
    Map<String, String> rules = new HashMap<String, String>();
    rules.put("*", "%2A");
    rules.put("+", "%20");
    rules.put("%7E", "~");
    ENCODING_RULES = Collections.unmodifiableMap(rules);
  }

  public static String encode(String plain)
  {
    Preconditions.checkNotNull(plain, "Cannot encode null object");
    String encoded = "";
    try
    {
      encoded = URLEncoder.encode(plain, CHARSET);
    }
    catch (UnsupportedEncodingException uee)
    {
      throw new OAuthException("Charset not found while encoding string: " + CHARSET, uee);
    }
    for(Map.Entry<String, String> rule : ENCODING_RULES.entrySet())
    {
      encoded = applyRule(encoded, rule.getKey(), rule.getValue());
    }
    return encoded;
  }

  private static String applyRule(String encoded, String toReplace, String replacement)
  {
    return encoded.replaceAll(Pattern.quote(toReplace), replacement);
  }

  public static String decode(String encoded)
  {
    Preconditions.checkNotNull(encoded, "Cannot decode null object");
    try
    {
      return URLDecoder.decode(encoded, CHARSET);
    }
    catch(UnsupportedEncodingException uee)
    {
      throw new OAuthException("Charset not found while decoding string: " + CHARSET, uee);
    }
  }
}
