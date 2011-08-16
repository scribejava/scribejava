package org.scribe.utils;

import java.util.*;

/**
 * Utils for {@link Map} manipulation
 * 
 * @author Pablo Fernandez
 */
public class MapUtils
{
  private static final String EMPTY_STRING = "";
  private static final String PAIR_SEPARATOR = "=";
  private static final String PARAM_SEPARATOR = "&";

  /**
   * Sorts a Map
   * 
   * @param map unsorted map
   * @return sorted map
   */
  public static final Map<String, String> sort(Map<String, String> map)
  {
    Preconditions.checkNotNull(map, "Cannot sort a null object.");

    Map<String, String> sorted = new LinkedHashMap<String, String>();
    for (String key : getSortedKeys(map))
    {
      sorted.put(key, map.get(key));
    }
    return sorted;
  }

  private static List<String> getSortedKeys(Map<String, String> map)
  {
    List<String> keys = new ArrayList<String>(map.keySet());
    Collections.sort(keys);
    return keys;
  }

  /**
   * Form-urlDecodes and appends all keys from the source {@link Map} to the
   * target {@link Map}
   *
   * @param source Map from where the keys get copied and decoded
   * @param target Map where the decoded keys are copied to
   */
  public static void decodeAndAppendEntries(Map<String, String> source, Map<String, String> target)
  {
    for (String key: source.keySet())
    {
      target.put(URLUtils.percentEncode(key), URLUtils.percentEncode(source.get(key)));
    }
  }

  /**
   * Concats a key-value map into a querystring-like String
   *
   * @param params key-value map
   * @return querystring-like String
   */
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
  public static Map<String, String> queryStringToMap(String queryString)
  {
    Map<String, String> result = new HashMap<String, String>();
    if (queryString != null && queryString.length() > 0)
    {
      for (String param : queryString.split(PARAM_SEPARATOR))
      {
        String pair[] = param.split(PAIR_SEPARATOR);
        String key = URLUtils.formURLDecode(pair[0]);
        String value = pair.length > 1 ? URLUtils.formURLDecode(pair[1]) : EMPTY_STRING;
        result.put(key, value);
      }
    }
    return result;
  }
}
