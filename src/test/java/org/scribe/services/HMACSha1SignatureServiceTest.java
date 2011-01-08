package org.scribe.services;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.exceptions.*;

public class HMACSha1SignatureServiceTest
{

  private HMACSha1SignatureService service;

  @Before
  public void setup()
  {
    service = new HMACSha1SignatureService();
  }

  @Test
  public void shouldReturnSignatureMethodString()
  {
    String expected = "HMAC-SHA1";
    assertEquals(expected, service.getSignatureMethod());
  }

  @Test
  public void shouldReturnSignature()
  {
    String apiSecret = "api secret";
    String tokenSecret = "token secret";
    String baseString = "base string";
    String signature = "uGymw2KHOTWI699YEaoi5xyLT50=";
    assertEquals(signature, service.getSignature(baseString, apiSecret, tokenSecret));
  }

  @Test(expected = OAuthException.class)
  public void shouldThrowExceptionIfBaseStringIsNull()
  {
    service.getSignature(null, "apiSecret", "tokenSecret");
  }

  @Test(expected = OAuthException.class)
  public void shouldThrowExceptionIfBaseStringIsEmpty()
  {
    service.getSignature("  ", "apiSecret", "tokenSecret");
  }

  @Test(expected = OAuthException.class)
  public void shouldThrowExceptionIfApiSecretIsNull()
  {
    service.getSignature("base string", null, "tokenSecret");
  }

  @Test(expected = OAuthException.class)
  public void shouldThrowExceptionIfApiSecretIsEmpty()
  {
    service.getSignature("base string", "  ", "tokenSecret");
  }
}
