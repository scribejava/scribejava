package org.scribe.utils;

import java.util.*;

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
      sorted.put(key, getSortedValues(map.get(key)));
    }
    return sorted;
  }

  private static List<String> getSortedKeys(Map<String, List<String>> map)
  {
    List<String> keys = new ArrayList<String>(map.keySet());
    Collections.sort(keys);
    return keys;
  }

  private static List<String> getSortedValues(List<String> values)
  {
    List<String> sortedValues = new ArrayList<String>(values);
    Collections.sort(sortedValues);
    return sortedValues;
  }

  /**
   * Form-urlEncodes and appends all keys from the source {@link Map} to the
   * target {@link Map}
   *
   * @param source Map from where the keys get copied and encoded
   * @param target Map where the encoded keys are copied to
   */
  public static void encodeAndAppendEntries(Map<String, String> source, Map<String, List<String>> target)
  {
    for (String key: source.keySet())
    {
      List<String> targetValues = target.get(key);
      if (targetValues == null)
      {
        targetValues = new ArrayList<String>();
      }
      targetValues.add(URLUtils.percentEncode(source.get(key)));
      target.put(URLUtils.percentEncode(key), targetValues);
    }
  }

  /**
   * Form-urlEncodes and appends all keys from the source {@link Map} to the
   * target {@link Map}
   *
   * @param source Map from where the keys get copied and encoded
   * @param target Map where the encoded keys are copied to
   */
  public static void encodeAndAppendMultiValueEntries(Map<String, List<String>> source, Map<String, List<String>> target)
  {
    encodeAndAppendMultiValueEntries(source, target, true);
  }

  /**
   * Appends all keys from the source {@link Map} to the
   * target {@link Map}
   *
   * @param source Map from where the keys get copied
   * @param target Map where the keys are copied to
   */
  public static void appendMultiValueEntries(Map<String, List<String>> source, Map<String, List<String>> target)
  {
    encodeAndAppendMultiValueEntries(source, target, false);
  }

  /**
   * Form-urlEncodes and appends all keys from the source {@link Map} to the
   * target {@link Map}
   *
   * @param source Map from where the keys get copied and encoded
   * @param target Map where the encoded keys are copied to
   * @param isEncode percent encode Map keys and values if true
   */
  private static void encodeAndAppendMultiValueEntries(Map<String, List<String>> source, 
          Map<String, List<String>> target, boolean isEncode)
  {
    for (String key: source.keySet())
    {
      List<String> sourceValues = source.get(key);
      List<String> targetValues = target.get(key);
      if (targetValues == null)
      {
        targetValues = new ArrayList<String>();
      }
      for (String value : sourceValues)
      {
        String encodedValue = value;
        if (isEncode)
        {
          encodedValue = URLUtils.percentEncode(value);
        }
        targetValues.add(encodedValue);
      }
      String encodedKey = key;
      if (isEncode)
      {
        encodedKey = URLUtils.percentEncode(key);
      }
      target.put(encodedKey, targetValues);
    }
  }
}
