package com.github.scribejava.core.utils;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author: Pablo Fernandez
 */
public class MapUtilsTest {

    @Test
    public void shouldPrettyPrintMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        Assert.assertEquals("{ 1 -> one , 2 -> two , 3 -> three , 4 -> four }", MapUtils.toString(map));
    }

    @Test
    public void shouldHandleEmptyMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        Assert.assertEquals("{}", MapUtils.toString(map));
    }

    @Test
    public void shouldHandleNullInputs() {
        Assert.assertEquals("", MapUtils.toString(null));
    }
}
