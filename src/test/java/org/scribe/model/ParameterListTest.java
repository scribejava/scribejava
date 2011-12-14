package org.scribe.model;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.utils.URLUtils;

/**
 * @author: Pablo Fernandez
 */
public class ParameterListTest
{
  private ParameterList params;

  @Before
  public void setup()
  {
    this.params = new ParameterList();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenAppendingNullMapToQuerystring()
  {
    String url = null;
    params.appendTo(url);
  }

  @Test
  public void shouldAppendNothingToQuerystringIfGivenEmptyMap()
  {
    String url = "http://www.example.com";
    Assert.assertEquals(url, params.appendTo(url));
  }

  @Test
  public void shouldAppendParametersToSimpleUrl()
  {
    String url = "http://www.example.com";
    String expectedUrl = "http://www.example.com?param1=value1&param2=value%20with%20spaces";

    params.add("param1", "value1");
    params.add("param2", "value with spaces");

    url = params.appendTo(url);
    Assert.assertEquals(url, expectedUrl);
  }

  @Test
  public void shouldAppendParametersToUrlWithQuerystring()
  {
    String url = "http://www.example.com?already=present";
    String expectedUrl = "http://www.example.com?already=present&param1=value1&param2=value%20with%20spaces";

    params.add("param1", "value1");
    params.add("param2", "value with spaces");

    url = params.appendTo(url);
    Assert.assertEquals(url, expectedUrl);
  }
}
