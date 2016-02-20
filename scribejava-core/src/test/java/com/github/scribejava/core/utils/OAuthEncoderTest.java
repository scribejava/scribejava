package com.github.scribejava.core.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OAuthEncoderTest {

    @Test
    public void shouldPercentEncodeString() {
        final String plain = "this is a test &^";
        final String encoded = "this%20is%20a%20test%20%26%5E";
        assertEquals(encoded, OAuthEncoder.encode(plain));
    }

    @Test
    public void shouldFormURLDecodeString() {
        final String encoded = "this+is+a+test+%26%5E";
        final String plain = "this is a test &^";
        assertEquals(plain, OAuthEncoder.decode(encoded));
    }

    @Test
    public void shouldPercentEncodeAllSpecialCharacters() {
        final String plain = "!*'();:@&=+$,/?#[]";
        final String encoded = "%21%2A%27%28%29%3B%3A%40%26%3D%2B%24%2C%2F%3F%23%5B%5D";
        assertEquals(encoded, OAuthEncoder.encode(plain));
        assertEquals(plain, OAuthEncoder.decode(encoded));
    }

    @Test
    public void shouldNotPercentEncodeReservedCharacters() {
        final String plain = "abcde123456-._~";
        final String encoded = plain;
        assertEquals(encoded, OAuthEncoder.encode(plain));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfStringToEncodeIsNull() {
        OAuthEncoder.encode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfStringToDecodeIsNull() {
        OAuthEncoder.decode(null);
    }

    @Test
    public void shouldPercentEncodeCorrectlyTwitterCodingExamples() {
        // These tests are part of the Twitter dev examples here
        // -> https://dev.twitter.com/docs/auth/percent-encoding-parameters
        final String[] sources = {"Ladies + Gentlemen", "An encoded string!", "Dogs, Cats & Mice"};
        final String[] encoded = {"Ladies%20%2B%20Gentlemen", "An%20encoded%20string%21",
            "Dogs%2C%20Cats%20%26%20Mice"};

        for (int i = 0; i < sources.length; i++) {
            assertEquals(encoded[i], OAuthEncoder.encode(sources[i]));
        }
    }
}
