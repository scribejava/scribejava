package org.scribe.services;

import org.apache.commons.codec.binary.*;
import org.scribe.exceptions.*;
import java.security.*;

/**
 * A signature service that uses the RSA-SHA1 algorithm.
 */
public class RSASha1SignatureService implements SignatureService
{
  private static final String METHOD = "RSA-SHA1";
  private static final String RSA_SHA1 = "SHA1withRSA";

  private PrivateKey privateKey;

  public RSASha1SignatureService(PrivateKey privateKey)
  {
    this.privateKey = privateKey;
  }

  /**
   * {@inheritDoc}
   */
  public String getSignature(String baseString, String apiSecret, String tokenSecret)
  {
    try
    {
      Signature signature = Signature.getInstance(RSA_SHA1);
      signature.initSign(privateKey);
      signature.update(baseString.getBytes());
      return new String(Base64.encodeBase64(signature.sign(), false));
    }
    catch (Exception e)
    {
      throw new OAuthSignatureException(baseString, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getSignatureMethod()
  {
    return METHOD;
  }
}