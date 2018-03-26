package com.github.scribejava.core.services;

import com.github.scribejava.core.java8.Base64;

/**
 * Signs a base string, returning the OAuth signature
 * https://tools.ietf.org/html/rfc5849#section-3.4
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
