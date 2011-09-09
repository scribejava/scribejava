package org.scribe.utils;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class MapUtilsTest
{

  private List<Param> unsorted;

  @Before
  public void setup()
  {
    unsorted = new ArrayList<Param>();
    unsorted.add(new Param("d", "fourth"));
    unsorted.add(new Param("a", "first"));
    unsorted.add(new Param("c", "third"));
    unsorted.add(new Param("b", "second"));
  }

  @Test
  public void shouldSortMap()
  {
    List<Param> sorted = MapUtils.sort(unsorted);
    assertEquals("first", sorted.get(0).getValue());
    assertEquals("second", sorted.get(1).getValue());
    assertEquals("third", sorted.get(2).getValue());
    assertEquals("fourth", sorted.get(3).getValue());
  }

  @Test
  public void shouldNotModifyTheOriginalMap()
  {
    List<Param> sorted = MapUtils.sort(unsorted);
    assertNotSame(sorted, unsorted);

    assertEquals("first", unsorted.get(0).getValue());
    assertEquals("second", unsorted.get(1).getValue());
    assertEquals("third", unsorted.get(2).getValue());
    assertEquals("fourth", unsorted.get(3).getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullMap()
  {
    MapUtils.sort(null);
  }
}
