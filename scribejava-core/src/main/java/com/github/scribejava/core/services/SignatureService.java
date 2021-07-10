package com.github.scribejava.core.services;

/**
 * Signs a base string, returning the OAuth signature https://tools.ietf.org/html/rfc5849#section-3.4
 */
public interface SignatureService {

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
