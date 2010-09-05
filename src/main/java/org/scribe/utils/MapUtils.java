package org.scribe.utils;

import java.util.*;

public class MapUtils
{

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
}
