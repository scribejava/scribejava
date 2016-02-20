package com.github.scribejava.core.utils;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class MapUtilsTest {

    @Test
    public void shouldPrettyPrintMap() {
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        Assert.assertEquals("{ 1 -> one , 2 -> two , 3 -> three , 4 -> four }", MapUtils.toString(map));
    }

    @Test
    public void shouldHandleEmptyMap() {
        final Map<Integer, String> map = new HashMap<>();
        Assert.assertEquals("{}", MapUtils.toString(map));
    }

    @Test
    public void shouldHandleNullInputs() {
        Assert.assertEquals("", MapUtils.toString(null));
    }
}
