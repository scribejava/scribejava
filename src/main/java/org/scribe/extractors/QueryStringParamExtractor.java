package org.scribe.extractors;

import java.util.HashMap;
import java.util.Map;

import org.scribe.utils.Preconditions;

public class QueryStringParamExtractor
{
  /**
   * @param queryString a string of the form name=value&name2=value2
   * @return a map of the form {name => value, name2 => value2 ...}
   */
  public static Map<String, String> extract(String queryString)
  {
    Map<String, String> params = new HashMap<String, String>();
    String[] parts = queryString.split("&");
    for(String part : parts) {
      String[] nameValue = part.split("=");
      Preconditions.check(nameValue.length == 2, "Malformed query string: " + queryString);
      params.put(nameValue[0], nameValue[1]);
    }
    
    return params;
  }
}
