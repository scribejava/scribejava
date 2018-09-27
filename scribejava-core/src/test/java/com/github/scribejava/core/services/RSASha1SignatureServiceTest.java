package com.github.scribejava.core.services;

import com.github.scribejava.core.java8.Base64;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RSASha1SignatureServiceTest {

    private final RSASha1SignatureService service = new RSASha1SignatureService(getPrivateKey());

    @Test
    public void shouldReturnSignatureMethodString() {
        final String expected = "RSA-SHA1";
        assertEquals(expected, service.getSignatureMethod());
    }

    @Test
    public void shouldReturnSignature() {
        final String apiSecret = "api secret";
        final String tokenSecret = "token secret";
        final String baseString = "base string";
        final String signature = "LUNRzQAlpdNyM9mLXm96Va6g/qVNnEAb7p7K1KM0g8IopOFQJPoOO7cvppgt7w3QyhijWJnCmvqXaaIAGrqvd"
                + "yr3fIzBULh8D/iZQUNLMi08GCOA34P81XBvsc7A5uJjPDsGhJg2MzoVJ8nWJhU/lMMk4c92S1WGskeoDofRwpo=";
        assertEquals(signature, service.getSignature(baseString, apiSecret, tokenSecret));
    }

    /**
     * Created primary key using openssl.
     *
     * openssl req -x509 -nodes -days 365 -newkey rsa:1024 -sha1 -subj '/C=GB/ST=/L=Manchester/CN=www.example.com'
     * -keyout myrsakey.pem -out /tmp/myrsacert.pem openssl pkcs8 -in myrsakey.pem -topk8 -nocrypt -out myrsakey.pk8
     */
    private static PrivateKey getPrivateKey() {
        final String str = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPQ5BCMxlUq2TYy\n"
                + "iRIoEUsz6HGTJhHuasS2nx1Se4Co3lxwxyubVdFj8AuhHNJSmJvjlpbTsGOjLZpr\n"
                + "HyDEDdJmf1Fensh1MhUnBZ4a7uLrZrKzFHHJdamX9pxapB89vLeHlCot9hVXdrZH\n"
                + "nNtg6FdmRKH/8gbs8iDyIayFvzYDAgMBAAECgYA+c9MpTBy9cQsR9BAvkEPjvkx2\n"
                + "XL4ZnfbDgpNA4Nuu7yzsQrPjPomiXMNkkiAFHH67yVxwAlgRjyuuQlgNNTpKvyQt\n"
                + "XcHxffnU0820VmE23M+L7jg2TlB3+rUnEDmDvCoyjlwGDR6lNb7t7Fgg2iR+iaov\n"
                + "0iVzz+l9w0slRlyGsQJBAPWXW2m3NmFgqfDxtw8fsKC2y8o17/cnPjozRGtWb8LQ\n"
                + "g3VCb8kbOFHOYNGazq3M7+wD1qILF2h/HecgK9eQrZ0CQQDMHXoJMfKKbrFrTKgE\n"
                + "zyggO1gtuT5OXYeFewMEb5AbDI2FfSc2YP7SHij8iQ2HdukBrbTmi6qxh3HmIR58\n"
                + "I/AfAkEA0Y9vr0tombsUB8cZv0v5OYoBZvCTbMANtzfb4AOHpiKqqbohDOevLQ7/\n"
                + "SpvgVCmVaDz2PptcRAyEBZ5MCssneQJAB2pmvaDH7Ambfod5bztLfOhLCtY5EkXJ\n"
                + "n6rZcDbRaHorRhdG7m3VtDKOUKZ2DF7glkQGV33phKukErVPUzlHBwJAScD9TqaG\n"
                + "wJ3juUsVtujV23SnH43iMggXT7m82STpPGam1hPfmqu2Z0niePFo927ogQ7H1EMJ\n"
                + "UHgqXmuvk2X/Ww==";

        try {
            final KeyFactory fac = KeyFactory.getInstance("RSA");
            final PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(str));
            return fac.generatePrivate(privKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
