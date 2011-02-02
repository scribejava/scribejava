package org.scribe.model;


import org.junit.*;

public class VerifierTest
{

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorForNullString()
  {
    new Verifier(null);
  }
}
