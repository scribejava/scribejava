package com.github.scribejava.core.services;

import com.github.scribejava.core.exceptions.OAuthException;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HMACSha256SignatureServiceTest {

    private HMACSha256SignatureService service;

    @Before
    public void setUp() {
        service = new HMACSha256SignatureService();
    }

    @Test
    public void shouldReturnSignatureMethodString() {
        final String expected = "HMAC-SHA256";
        assertEquals(expected, service.getSignatureMethod());
    }

    @Test
    public void shouldReturnSignature() {
        final String apiSecret = "apiSecret";
        final String tokenSecret = "tokenSecret";
        final String baseString = "baseString";
        final String signature = "xDJIQbKJTwGumZFvSG1V3ctym2tz6kD8fKGWPr5ImPU=";
        assertEquals(signature, service.getSignature(baseString, apiSecret, tokenSecret));
    }

    @Test
    public void shouldThrowExceptionIfBaseStringIsNull() {
        assertThrows(OAuthException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                service.getSignature(null, "apiSecret", "tokenSecret");
            }
        });
    }

    @Test
    public void shouldThrowExceptionIfBaseStringIsEmpty() {
        assertThrows(OAuthException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                service.getSignature(" ", "apiSecret", "tokenSecret");
            }
        });
    }

    @Test
    public void shouldThrowExceptionIfApiSecretIsNull() {
        assertThrows(OAuthException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                service.getSignature("baseString", null, "tokenSecret");
            }
        });
    }

    @Test
    public void shouldNotThrowExceptionIfApiSecretIsEmpty() {
        final String apiSecret = " ";
        final String tokenSecret = "tokenSecret";
        final String baseString = "baseString";
        final String signature = "VRZvKmNIgbjfwPOoCPEvxFUV23v0BopcE6OE9P2Odz0=";
        assertEquals(signature, service.getSignature(baseString, apiSecret, tokenSecret));
    }
}
