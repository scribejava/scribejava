package org.scribe.model;

import static junit.framework.Assert.*;
import org.junit.*;

public class TokenTest
{
  @Test
  public void shouldTestEqualityBasedOnTokenAndSecret() throws Exception
  {
    Token expected = new Token("access","secret");
    Token actual = new Token("access","secret");

    assertEquals(expected, actual);
    assertEquals(actual, actual);
  }

  @Test
  public void shouldNotDependOnRawString() throws Exception
  {
    Token expected = new Token("access","secret", "raw_string");
    Token actual = new Token("access","secret", "different_raw_string");

    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnSameHashCodeForEqualObjects() throws Exception
  {
    Token expected = new Token("access","secret");
    Token actual = new Token("access","secret");

    assertEquals(expected.hashCode(), actual.hashCode());
  }

  @Test
  public void shouldNotBeEqualToNullOrOtherObjects() throws Exception
  {
    Token expected = new Token("access","secret","response");

    assertNotSame(expected, null);
    assertNotSame(expected, new Object());
  }
}
