package org.scribe.model;

import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class TokenTest {

  @Test
  public void testEquals() throws Exception
  {
    Token expected = new Token("access","secret","response");
    Token actual = new Token("access","secret","response");
    Assert.assertEquals(expected,actual);
    Assert.assertEquals(expected,expected);
  }

  @Test
  public void testEqualsNotEqual() throws Exception
  {
    Token expected = new Token("access","secret","response");

    assertFalse(expected.equals(new Token("DIFFERENT","secret","response")));
    assertFalse(expected.equals(new Token("access","DIFFERENT","response")));
    assertFalse(expected.equals(new Token("access","secret","DIFFERENT")));
    assertFalse(expected.equals(new Token(null,"secret","response")));
    assertFalse(expected.equals(new Token("access",null,"response")));
    assertFalse(expected.equals(new Token("access","secret",null)));
  }

  @Test
  public void testHashCode() throws Exception
  {
    Token expected = new Token("access","secret","response");
    Token actual = new Token("access","secret","response");
    Assert.assertEquals(expected.hashCode(), actual.hashCode());
    Assert.assertEquals(expected.hashCode(), expected.hashCode());
  }

  @Test
  public void testHashCodeWhenNotEquals() throws Exception
  {
    Token expected = new Token("access","secret","response");

    assertFalse(expected.hashCode() == new Token("DIFFERENT","secret","response").hashCode());
    assertFalse(expected.hashCode() == new Token("access","DIFFERENT","response").hashCode());
    assertFalse(expected.hashCode() == new Token("access","secret","DIFFERENT").hashCode());
    assertFalse(expected.hashCode() == new Token(null,"secret","response").hashCode());
    assertFalse(expected.hashCode() == new Token("access",null,"response").hashCode());
    assertFalse(expected.hashCode() == new Token("access","secret",null).hashCode());
  }
}
