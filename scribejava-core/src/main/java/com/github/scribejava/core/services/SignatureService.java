package com.github.scribejava.core.services;

import java.util.Base64;

/**
 * Signs a base string, returning the OAuth signature
 */
public interface SignatureService {
    Base64.Encoder BASE_64_ENCODER = Base64.getEncoder();

    /**
     * Returns the signature
     *
     * @param baseString url-encoded string to sign
     * @param apiSecret api secret for your app
     * @param tokenSecret token secret (empty string for the request token step)
     *
     * @return signature
     */
    String getSignature(String baseString, String apiSecret, String tokenSecret);

    String getSignatureMethod();
}
