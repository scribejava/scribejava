package com.github.scribejava.core.services;

import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import com.github.scribejava.core.exceptions.OAuthSignatureException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * A signature service that uses the RSA-SHA1 algorithm.
 */
public class RSASha1SignatureService implements SignatureService {

    private static final String METHOD = "RSA-SHA1";
    private static final String RSA_SHA1 = "SHA1withRSA";
    private static final String UTF8 = "UTF-8";

    private final PrivateKey privateKey;

    public RSASha1SignatureService(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            final Signature signature = Signature.getInstance(RSA_SHA1);
            signature.initSign(privateKey);
            signature.update(baseString.getBytes(UTF8));
            return BASE_64_ENCODER.encodeToString(signature.sign());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | UnsupportedEncodingException |
                RuntimeException e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignatureMethod() {
        return METHOD;
    }
}
