package org.scribe.services;

import org.scribe.exceptions.*;
import java.security.*;

// implementation of base64 encoding lives here.
import javax.xml.bind.DatatypeConverter;

/**
 * A signature service that uses the RSA-SHA1 algorithm.
 */
public class RSASha1SignatureService implements SignatureService
{
  private static final String METHOD = "RSA-SHA1";
  private static final String RSA_SHA1 = "SHA1withRSA";
  private static final String UTF8 = "UTF-8";

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
      signature.update(baseString.getBytes(UTF8));
      return bytesToBase64String(signature);
    }
    catch (Exception e)
    {
      throw new OAuthSignatureException(baseString, e);
    }
  }

  private String bytesToBase64String(Signature signature) throws SignatureException
  {
    return DatatypeConverter.printBase64Binary(signature.sign());
  }

  /**
   * {@inheritDoc}
   */
  public String getSignatureMethod()
  {
    return METHOD;
  }
}