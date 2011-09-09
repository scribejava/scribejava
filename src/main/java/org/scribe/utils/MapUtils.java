package org.scribe.utils;

import java.util.*;
import java.util.Map.Entry;

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
  public static final List<Param> sort(Collection<Param> map)
  {
    Preconditions.checkNotNull(map, "Cannot sort a null object.");

    List<Param> sorted = new ArrayList<Param>(map);
    Collections.sort(sorted);
    return sorted;
  }

  /**
   * Form-urlDecodes and appends all keys from the source {@link Map} to the
   * target {@link Map}
   *
   * @param source Map from where the keys get copied and decoded
   * @param target Map where the decoded keys are copied to
   */
  public static void decodeAndAppendEntries(List<Param> source, List<Param> target)
  {
    for (Entry<String, String> entry: source)
    {
      target.add(new Param(URLUtils.percentEncode(entry.getKey()), URLUtils.percentEncode(entry.getValue())));
    }
  }
}
