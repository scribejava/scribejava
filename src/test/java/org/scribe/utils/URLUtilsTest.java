package org.scribe.utils;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import static java.util.Collections.*;

public class URLUtilsTest
{
  @Test
  public void shouldPercentEncodeMap()
  {
    Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
    params.put("key", singletonList("value"));
    params.put("key with spaces", singletonList("value with spaces"));
    params.put("&symbols!", singletonList("#!"));

    String expected = "key=value&key+with+spaces=value+with+spaces&%26symbols%21=%23%21";
    assertEquals(expected, URLUtils.formURLEncodeMultiValuedMap(params));
  }

  @Test
  public void shouldReturnEmptyStringForEmptyMap()
  {
    Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
    String expected = "";
    assertEquals(expected, URLUtils.formURLEncodeMultiValuedMap(params));
  }

  @Test
  public void shouldFormURLEncodeMapWithMissingValues()
  {
    Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
    params.put("key", singletonList("value"));
    params.put("key with spaces", singletonList((String)null));

    String expected = "key=value&key+with+spaces";
    assertEquals(expected, URLUtils.formURLEncodeMultiValuedMap(params));
  }

  @Test
  public void shouldPercentEncodeString()
  {
    String toEncode = "this is a test &^";
    String expected = "this%20is%20a%20test%20%26%5E";
    assertEquals(expected, URLUtils.percentEncode(toEncode));
  }

  @Test
  public void shouldFormURLEncodeString()
  {
    String toEncode = "this is a test &^";
    String expected = "this+is+a+test+%26%5E";
    assertEquals(expected, URLUtils.formURLEncode(toEncode));
  }

  @Test
  public void shouldFormURLDecodeString()
  {
    String toDecode = "this+is+a+test+%26%5E";
    String expected = "this is a test &^";
    assertEquals(expected, URLUtils.formURLDecode(toDecode));
  }

  @Test
  public void shouldPercentEncodeAllSpecialCharacters()
  {
    String plain = "!*'();:@&=+$,/?#[]";
    String encoded = "%21%2A%27%28%29%3B%3A%40%26%3D%2B%24%2C%2F%3F%23%5B%5D";
    assertEquals(encoded, URLUtils.percentEncode(plain));
    assertEquals(plain, URLUtils.formURLDecode(encoded));
  }

  @Test
  public void shouldNotPercentEncodeReservedCharacters()
  {
    String plain = "abcde123456-._~";
    String encoded = plain;
    assertEquals(encoded, URLUtils.percentEncode(plain));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfMapIsNull()
  {
    Map<String, List<String>> nullMap = null;
    URLUtils.formURLEncodeMultiValuedMap(nullMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfStringToEncodeIsNull()
  {
    String toEncode = null;
    URLUtils.percentEncode(toEncode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfStringToDecodeIsNull()
  {
    String toDecode = null;
    URLUtils.formURLDecode(toDecode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenAppendingNullMapToQuerystring()
  {
    String url = "http://www.example.com";
    Map<String, List<String>> nullMap = null;
    URLUtils.appendParametersToQueryString(url, nullMap);
  }

  @Test
  public void shouldAppendNothingToQuerystringIfGivenEmptyMap()
  {
    String url = "http://www.example.com";
    Map<String, List<String>> emptyMap = new HashMap<String, List<String>>();
    String newUrl = URLUtils.appendParametersToQueryString(url, emptyMap);
    Assert.assertEquals(url, newUrl);
  }

  @Test
  public void shouldAppendParametersToSimpleUrl()
  {
    String url = "http://www.example.com";
    String expectedUrl = "http://www.example.com?param1=value1&param2=value+with+spaces";

    Map<String, List<String>> params = new HashMap<String, List<String>>();
    params.put("param1", singletonList("value1"));
    params.put("param2", singletonList("value with spaces"));

    url = URLUtils.appendParametersToQueryString(url, params);
    Assert.assertEquals(url, expectedUrl);
  }

  @Test
  public void shouldAppendParametersToUrlWithQuerystring()
  {
    String url = "http://www.example.com?already=present";
    String expectedUrl = "http://www.example.com?already=present&param1=value1&param2=value+with+spaces";

    Map<String, List<String>> params = new HashMap<String, List<String>>();
    params.put("param1", singletonList("value1"));
    params.put("param2", singletonList("value with spaces"));

    url = URLUtils.appendParametersToQueryString(url, params);
    Assert.assertEquals(url, expectedUrl);
  }

  @Test
  public void shouldPercentEncodePlusSymbol()
  {
    String plain = "7aEP+jNAwvjc0mjhqg0nuXPf";
    String encoded = "7aEP%2BjNAwvjc0mjhqg0nuXPf";

    Assert.assertEquals(encoded, URLUtils.percentEncode(plain));
  }

  @Test
  public void shouldURLDecodePlusSymbol()
  {
    String encoded = "oauth_verifier=7aEP%2BjNAwvjc0mjhqg0nuXPf";
    String expected = "oauth_verifier=7aEP+jNAwvjc0mjhqg0nuXPf";

    Assert.assertEquals(expected, URLUtils.formURLDecode(encoded));
  }
}
