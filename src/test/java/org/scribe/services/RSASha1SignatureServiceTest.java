package org.scribe.services;

import static org.junit.Assert.*;

import org.junit.*;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.*;

public class RSASha1SignatureServiceTest
{

	RSASha1SignatureService service = new RSASha1SignatureService(getPrivateKey());

  @Test
  public void shouldReturnSignatureMethodString()
  {
    String expected = "RSA-SHA1";
    assertEquals(expected, service.getSignatureMethod());
  }

	@Test
  public void shouldReturnSignature()
  {
    String apiSecret = "api secret";
    String tokenSecret = "token secret";
    String baseString = "base string";
    String signature = "LUNRzQAlpdNyM9mLXm96Va6g/qVNnEAb7p7K1KM0g8IopOFQJPoOO7cvppgt7w3QyhijWJnCmvqXaaIAGrqvdyr3fIzBULh8D/iZQUNLMi08GCOA34P81XBvsc7A5uJjPDsGhJg2MzoVJ8nWJhU/lMMk4c92S1WGskeoDofRwpo=";
    assertEquals(signature, service.getSignature(baseString, apiSecret, tokenSecret));
  }

  /**
   *Created primary key using openssl.
   *
   * openssl req -x509 -nodes -days 365 -newkey rsa:1024 -sha1 -subj   '/C=GB/ST=/L=Manchester/CN=www.example.com' -keyout   myrsakey.pem -out /tmp/myrsacert.pem
   * openssl pkcs8 -in myrsakey.pem -topk8 -nocrypt -out myrsakey.pk8
   */
  private static PrivateKey getPrivateKey()
  {
    String str = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPQ5BCMxlUq2TYy\n"+
                 "iRIoEUsz6HGTJhHuasS2nx1Se4Co3lxwxyubVdFj8AuhHNJSmJvjlpbTsGOjLZpr\n"+
                 "HyDEDdJmf1Fensh1MhUnBZ4a7uLrZrKzFHHJdamX9pxapB89vLeHlCot9hVXdrZH\n"+
                 "nNtg6FdmRKH/8gbs8iDyIayFvzYDAgMBAAECgYA+c9MpTBy9cQsR9BAvkEPjvkx2\n"+
                 "XL4ZnfbDgpNA4Nuu7yzsQrPjPomiXMNkkiAFHH67yVxwAlgRjyuuQlgNNTpKvyQt\n"+
                 "XcHxffnU0820VmE23M+L7jg2TlB3+rUnEDmDvCoyjlwGDR6lNb7t7Fgg2iR+iaov\n"+
                 "0iVzz+l9w0slRlyGsQJBAPWXW2m3NmFgqfDxtw8fsKC2y8o17/cnPjozRGtWb8LQ\n"+
                 "g3VCb8kbOFHOYNGazq3M7+wD1qILF2h/HecgK9eQrZ0CQQDMHXoJMfKKbrFrTKgE\n"+
                 "zyggO1gtuT5OXYeFewMEb5AbDI2FfSc2YP7SHij8iQ2HdukBrbTmi6qxh3HmIR58\n"+
                 "I/AfAkEA0Y9vr0tombsUB8cZv0v5OYoBZvCTbMANtzfb4AOHpiKqqbohDOevLQ7/\n"+
                 "SpvgVCmVaDz2PptcRAyEBZ5MCssneQJAB2pmvaDH7Ambfod5bztLfOhLCtY5EkXJ\n"+
                 "n6rZcDbRaHorRhdG7m3VtDKOUKZ2DF7glkQGV33phKukErVPUzlHBwJAScD9TqaG\n"+
                 "wJ3juUsVtujV23SnH43iMggXT7m82STpPGam1hPfmqu2Z0niePFo927ogQ7H1EMJ\n"+
                 "UHgqXmuvk2X/Ww==";

    try
    {
      KeyFactory fac = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(str));
      return fac.generatePrivate(privKeySpec);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
