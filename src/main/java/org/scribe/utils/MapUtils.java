package org.scribe.utils;

import java.util.*;
import static java.util.Collections.*;

/**
 * Utils for {@link Map} manipulation
 * 
 * @author Pablo Fernandez
 */
public class MapUtils
{
  /**
   * Sorts a Map
   * 
   * @param map unsorted map
   * @return sorted map
   */
  public static final Map<String, List<String>> sort(Map<String, List<String>> map)
  {
    Preconditions.checkNotNull(map, "Cannot sort a null object.");

    Map<String, List<String>> sorted = new LinkedHashMap<String, List<String>>();
    for (String key : getSortedKeys(map))
    {
      sorted.put(key, map.get(key));
    }
    return sorted;
  }

  private static List<String> getSortedKeys(Map<String, ?> map)
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
  public static void decodeAndAppendMultiValuedEntries(Map<String, List<String>> source, Map<String, List<String>> target)
  {
    for (String key: source.keySet())
    {
      target.put(URLUtils.percentEncode(key), URLUtils.percentEncode(source.get(key)));
    }
  }
  
  public static void decodeAndAppendEntries(Map<String, String> source, Map<String, List<String>> target)
  {
    for (String key: source.keySet())
    {
      target.put(URLUtils.percentEncode(key), singletonList(URLUtils.percentEncode(source.get(key))));
    }
  }
}
