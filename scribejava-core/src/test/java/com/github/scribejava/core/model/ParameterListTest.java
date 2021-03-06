package com.github.scribejava.core.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import org.junit.function.ThrowingRunnable;

public class ParameterListTest {

    private ParameterList params;

    @Before
    public void setUp() {
        this.params = new ParameterList();
    }

    public void shouldThrowExceptionWhenAppendingNullMapToQuerystring() {
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                params.appendTo(null);
            }
        });
    }

    @Test
    public void shouldAppendNothingToQuerystringIfGivenEmptyMap() {
        final String url = "http://www.example.com";
        Assert.assertEquals(url, params.appendTo(url));
    }

    @Test
    public void shouldAppendParametersToSimpleUrl() {
        String url = "http://www.example.com";
        final String expectedUrl = "http://www.example.com?param1=value1&param2=value%20with%20spaces";

        params.add("param1", "value1");
        params.add("param2", "value with spaces");

        url = params.appendTo(url);
        Assert.assertEquals(expectedUrl, url);
    }

    @Test
    public void shouldAppendParametersToUrlWithQuerystring() {
        String url = "http://www.example.com?already=present";
        final String expectedUrl = "http://www.example.com?already=present&param1=value1&param2=value%20with%20spaces";

        params.add("param1", "value1");
        params.add("param2", "value with spaces");

        url = params.appendTo(url);
        Assert.assertEquals(expectedUrl, url);
    }

    @Test
    public void shouldProperlySortParameters() {
        params.add("param1", "v1");
        params.add("param6", "v2");
        params.add("a_param", "v3");
        params.add("param2", "v4");
        Assert.assertEquals("a_param=v3&param1=v1&param2=v4&param6=v2", params.sort().asFormUrlEncodedString());
    }

    @Test
    public void shouldProperlySortParametersWithTheSameName() {
        params.add("param1", "v1");
        params.add("param6", "v2");
        params.add("a_param", "v3");
        params.add("param1", "v4");
        Assert.assertEquals("a_param=v3&param1=v1&param1=v4&param6=v2", params.sort().asFormUrlEncodedString());
    }

    @Test
    public void shouldNotModifyTheOriginalParameterList() {
        params.add("param1", "v1");
        params.add("param6", "v2");

        assertNotSame(params, params.sort());
    }
}
