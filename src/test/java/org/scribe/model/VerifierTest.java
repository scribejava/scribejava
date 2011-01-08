package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.*;

public class VerifierTest
{
  @Test
  public void shouldParse()
  {
    Verifier verifier = new Verifier("p8k%2BGIjIL9PblXq%2BpH6LmT9l");
    assertEquals(verifier.getValue(), "p8k+GIjIL9PblXq+pH6LmT9l");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorForNullString()
  {
    new Verifier(null);
  }
}
