package org.scribe.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.OAuthEncoder;

/**
 * @author: Pablo Fernandez
 */
public class Parameter implements Comparable<Parameter>
{
  private static final String UTF = "UTF8";

  private final String key;
  private final String value;

  public Parameter(String key, String value)
  {
    this.key = key;
    this.value = value;
  }

  public String asUrlEncodedPair()
  {
    return OAuthEncoder.encode(key).concat("=").concat(OAuthEncoder.encode(value));
  }
  
  public boolean equals(Object other)
  {
    if(other == null) return false;
    if(other == this) return true;
    if(!(other instanceof Parameter)) return false;
    
    Parameter otherParam = (Parameter) other;
    return otherParam.key.equals(key) && otherParam.value.equals(value);
  }

  public int hashCode()
  {
    return key.hashCode() + value.hashCode();
  }

  public int compareTo(Parameter parameter)
  {
    int keyDiff = key.compareTo(parameter.key);

    return keyDiff != 0 ? keyDiff : value.compareTo(parameter.value);
  }
}
