package com.github.scribejava.core.services;

/**
 * Signs a base string, returning the OAuth signature
 *
 * @author Pablo Fernandez
 *
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
