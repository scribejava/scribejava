package com.github.scribejava.core.services;

/**
 * Unix epoch timestamp generator.
 *
 * This class is useful for stubbing in tests.
 *
 * @author Pablo Fernandez
 */
public interface TimestampService {

    /**
     * Returns the unix epoch timestamp in seconds
     *
     * @return timestamp
     */
    public String getTimestampInSeconds();

    /**
     * Returns a nonce (unique value for each request)
     *
     * @return nonce
     */
    public String getNonce();
}
