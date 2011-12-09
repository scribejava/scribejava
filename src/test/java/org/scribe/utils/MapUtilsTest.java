package org.scribe.utils;

import static org.junit.Assert.*;
import static java.util.Collections.*;
import static java.util.Arrays.*;

import java.util.*;

import org.junit.*;

public class MapUtilsTest
{

  private Map<String, List<String>> unsorted;

  @Before
  public void setup()
  {
    unsorted = new LinkedHashMap<String, List<String>>();
    unsorted.put("d", singletonList("fourth"));
    unsorted.put("a", singletonList("first"));
    unsorted.put("c", singletonList("third"));
    unsorted.put("b", singletonList("second"));
  }

  @Test
  public void shouldSortMap()
  {
    Map<String, List<String>> sorted = MapUtils.sort(unsorted);
    List<List<String>> values = new ArrayList<List<String>>(sorted.values());
    assertEquals(singletonList("first"), values.get(0));
    assertEquals(singletonList("second"), values.get(1));
    assertEquals(singletonList("third"), values.get(2));
    assertEquals(singletonList("fourth"), values.get(3));
  }

  @Test
  public void shouldNotModifyTheOriginalMap()
  {
    Map<String, List<String>> sorted = MapUtils.sort(unsorted);
    assertNotSame(sorted, unsorted);

    List<List<String>> values = new ArrayList<List<String>>(unsorted.values());
    assertEquals(singletonList("fourth"), values.get(0));
    assertEquals(singletonList("first"), values.get(1));
    assertEquals(singletonList("third"), values.get(2));
    assertEquals(singletonList("second"), values.get(3));
  }

  @Test
  public void decodeAndAppendPercentEncodesSingleValueEntries() {
      Map<String, String> source = new HashMap<String, String>();
      source.put("a", "one;");
      source.put("b", "two?");
      
      Map<String, List<String>> target = new HashMap<String, List<String>>();
      MapUtils.decodeAndAppendEntries(source, target);
      
      assertEquals(target.get("a"), asList("one%3B"));
      assertEquals(target.get("b"), asList("two%3F"));
  }

  @Test
  public void decodeAndAppendPercentEncodesMultiValueEntries() 
  {
    Map<String, List<String>> source = new HashMap<String, List<String>>();
    source.put("a", asList("one;", "two?"));
    source.put("b", asList("three!", "(four)"));
    
    Map<String, List<String>> target = new HashMap<String, List<String>>();
    MapUtils.decodeAndAppendMultiValuedEntries(source, target);
    
    assertEquals(target.get("a"), asList("one%3B", "two%3F"));
    assertEquals(target.get("b"), asList("three%21", "%28four%29"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullMap()
  {
    MapUtils.sort(null);
  }
}
