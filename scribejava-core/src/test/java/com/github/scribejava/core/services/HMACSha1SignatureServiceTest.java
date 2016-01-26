package com.github.scribejava.core.services;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthException;

public class HMACSha1SignatureServiceTest {

    private HMACSha1SignatureService service;

    @Before
    public void setUp() {
        service = new HMACSha1SignatureService();
    }

    @Test
    public void shouldReturnSignatureMethodString() {
        final String expected = "HMAC-SHA1";
        assertEquals(expected, service.getSignatureMethod());
    }

    @Test
    public void shouldReturnSignature() {
        final String apiSecret = "api secret";
        final String tokenSecret = "token secret";
        final String baseString = "base string";
        final String signature = "uGymw2KHOTWI699YEaoi5xyLT50=";
        assertEquals(signature, service.getSignature(baseString, apiSecret, tokenSecret));
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfBaseStringIsNull() {
        service.getSignature(null, "apiSecret", "tokenSecret");
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfBaseStringIsEmpty() {
        service.getSignature("  ", "apiSecret", "tokenSecret");
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfApiSecretIsNull() {
        service.getSignature("base string", null, "tokenSecret");
    }

    @Test(expected = OAuthException.class)
    public void shouldThrowExceptionIfApiSecretIsEmpty() {
        service.getSignature("base string", "  ", "tokenSecret");
    }
}
