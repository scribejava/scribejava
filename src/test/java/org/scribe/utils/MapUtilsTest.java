package org.scribe.utils;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class MapUtilsTest
{

  private Map<String, List<String>> unsorted;

  @Before
  public void setup()
  {
    unsorted = new LinkedHashMap<String, List<String>>();
    unsorted.put("d", Arrays.asList("fourth", "xyz", "abc"));
    unsorted.put("a", Arrays.asList("first"));
    unsorted.put("c", Arrays.asList("third"));
    unsorted.put("b", Arrays.asList("second"));
  }

  @Test
  public void shouldSortMap()
  {
    Map<String, List<String>> sorted = MapUtils.sort(unsorted);
    List<List<String>> values = new ArrayList<List<String>>(sorted.values());
    assertEquals("first", values.get(0).get(0));
    assertEquals("second", values.get(1).get(0));
    assertEquals("third", values.get(2).get(0));

    List<String> fourth = values.get(3);
    assertEquals("abc", fourth.get(0));
    assertEquals("fourth", fourth.get(1));
    assertEquals("xyz", fourth.get(2));
  }

  @Test
  public void shouldNotModifyTheOriginalMap()
  {
    Map<String, List<String>> sorted = MapUtils.sort(unsorted);
    assertNotSame(sorted, unsorted);

    List<List<String>> values = new ArrayList<List<String>>(unsorted.values());
    List<String> fourth = values.get(0);
    assertEquals("fourth", fourth.get(0));
    assertEquals("xyz", fourth.get(1));
    assertEquals("abc", fourth.get(2));
    
    assertEquals("first", values.get(1).get(0));
    assertEquals("third", values.get(2).get(0));
    assertEquals("second", values.get(3).get(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullMap()
  {
    MapUtils.sort(null);
  }
}
