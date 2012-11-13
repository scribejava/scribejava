package org.scribe.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

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

  @Test
  public void shouldAddParameter() {
	this.params.add("param1", "value1");
	Assert.assertEquals("param1=value1", params.asFormUrlEncodedString());
	final Parameter newParam = new Parameter("param2", "value2");  
	this.params.add(newParam);
	Assert.assertEquals("param1=value1&param2=value2", params.asFormUrlEncodedString());
  }
  
  @Test
  public void shouldProperlyUrlEncode() {
	this.params.add("param1", "value1");
	Assert.assertEquals("param1=value1", params.asFormUrlEncodedString());
  }

  @Test
  public void shouldProperlyMultiPartEncode() {
	final Parameter param = new Parameter("param1", "value1");
	this.params.add(param);
	final String boundary = ParameterList.getBoundary();
	final String paramSep = new StringBuilder("--").append(boundary).append(Parameter.SEQUENCE_NEW_LINE).toString();
	StringBuilder builder = new StringBuilder();
	builder.append(paramSep);
	//Add the parameters
	builder.append(param.asMultiPartEncodedString());
	//Add the boundrary.
	builder.append(paramSep);
	builder.append(Parameter.SEQUENCE_NEW_LINE);
	builder.append("--");
	builder.append(boundary);
	builder.append( "--" );
	builder.append(Parameter.SEQUENCE_NEW_LINE);
	final String multiPartStr = builder.toString();	
	Assert.assertEquals(multiPartStr, params.asMultiPartEncodedString());
  }

  @Test
  public void shouldMatchOAuthBaseString() {
	final String expBaseStr = "p1%3Dv1%26p2%3Dv2";
	//Add the paramters
	this.params.add("p1", "v1");
	this.params.add("p2", "v2");
	this.params.add(new FileParameter("fParam", null));
	Assert.assertEquals(expBaseStr, params.asOauthBaseString());
  }
  
  @Test
  public void shouldBeSameBoundary() {
	final String expBoundary = ParameterList.getBoundary();
	Assert.assertNotNull(expBoundary);
	Assert.assertSame(expBoundary, ParameterList.getBoundary());	
  }
  
  @Test
  public void shouldProperlySortParameters()
  {
    params.add("param1", "v1");
    params.add("param6", "v2");
    params.add("a_param", "v3");
    params.add("param2", "v4");
    Assert.assertEquals("a_param=v3&param1=v1&param2=v4&param6=v2", params.sort().asFormUrlEncodedString());
  }

  @Test
  public void shouldProperlySortParametersWithTheSameName()
  {
    params.add("param1", "v1");
    params.add("param6", "v2");
    params.add("a_param", "v3");
    params.add("param1", "v4");
    Assert.assertEquals("a_param=v3&param1=v1&param1=v4&param6=v2", params.sort().asFormUrlEncodedString());
  }

  @Test
  public void shouldNotModifyTheOriginalParameterList()
  {
    params.add("param1", "v1");
    params.add("param6", "v2");

    assertNotSame(params, params.sort());
  }
}
