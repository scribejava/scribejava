package org.scribe.services;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;

import static org.junit.Assert.*;

public class PlaintextSignatureServiceTest {

  private PlaintextSignatureService service;

  @Before
  public void setup()
  {
    service = new PlaintextSignatureService();
  }

  @Test
  public void getSignature() {
    String sig = service.getSignature("BaseString", "ApiSecret", "TokenSecret");
    assertEquals(sig, "ApiSecret&TokenSecret");
  }

  @Test(expected=OAuthException.class)
  public void getSignatureWithNullApiSecred() {
    service.getSignature("baseString", null, "tokenSecret");
  }

  @Test
  public void getSignatureMethod()
  {
    assertEquals("plaintext", service.getSignatureMethod());
  }

}
