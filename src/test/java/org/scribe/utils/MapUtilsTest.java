package org.scribe.utils;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class MapUtilsTest
{

  private Map<String, String> unsorted;

  @Before
  public void setup()
  {
    unsorted = new LinkedHashMap<String, String>();
    unsorted.put("d", "fourth");
    unsorted.put("a", "first");
    unsorted.put("c", "third");
    unsorted.put("b", "second");
  }

  @Test
  public void shouldSortMap()
  {
    Map<String, String> sorted = MapUtils.sort(unsorted);
    List<String> values = new ArrayList<String>(sorted.values());
    assertEquals("first", values.get(0));
    assertEquals("second", values.get(1));
    assertEquals("third", values.get(2));
    assertEquals("fourth", values.get(3));
  }

  @Test
  public void shouldNotModifyTheOriginalMap()
  {
    Map<String, String> sorted = MapUtils.sort(unsorted);
    assertNotSame(sorted, unsorted);

    List<String> values = new ArrayList<String>(unsorted.values());
    assertEquals("fourth", values.get(0));
    assertEquals("first", values.get(1));
    assertEquals("third", values.get(2));
    assertEquals("second", values.get(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullMap()
  {
    MapUtils.sort(null);
  }
}
