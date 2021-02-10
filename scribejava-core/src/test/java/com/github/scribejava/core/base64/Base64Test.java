package com.github.scribejava.core.base64;

import java.io.UnsupportedEncodingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {

    private Base64 java8Base64;
    private byte[] helloWorldBytes;
    private byte[] helloWorldTwoLinesBytes;
    private byte[] helloWorldTwoLinesAndNewLineBytes;
    private byte[] helloWorldDifferentCharsBytes;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        helloWorldBytes = "Hello World".getBytes("UTF-8");
        helloWorldTwoLinesBytes = "Hello World\r\nNew Line2".getBytes("UTF-8");
        helloWorldTwoLinesAndNewLineBytes = "Hello World\r\nSecond Line\r\n".getBytes("UTF-8");
        helloWorldDifferentCharsBytes = ("`1234567890-=~!@#$%^&*()_+ёЁ\"№;:?qwertyuiop[]asdfghjkl;'zxcvbnm,./QWERTYUIOP"
                + "{}|ASDFGHJKL:ZXCVBNM<>?йфяцычувскамепинртгоьшлбщдюзж.хэъ\\ЙФЯЦЫЧУВСКАМЕПИНРТГОЬШЛБЩДЮЗЖ,ХЭЪ/\r\t\f\'"
                + "\b\n").getBytes("UTF-8");
        java8Base64 = new Java8Base64();
    }

    @Test
    public void allImplementationsAreAvailable() {
        assertTrue(Java8Base64.isAvailable());
    }

    @Test
    public void testEncode() {
        final String helloWorldEncoded = "SGVsbG8gV29ybGQ=";
        final String helloWorldTwoLinesEncoded = "SGVsbG8gV29ybGQNCk5ldyBMaW5lMg==";
        final String helloWorldTwoLinesAndNewLineEncoded = "SGVsbG8gV29ybGQNClNlY29uZCBMaW5lDQo=";
        final String helloWorldDifferentCharsEncoded = "YDEyMzQ1Njc4OTAtPX4hQCMkJV4mKigpXyvRkdCBIuKEljs6P3F3ZXJ0eXVpb3B"
                + "bXWFzZGZnaGprbDsnenhjdmJubSwuL1FXRVJUWVVJT1B7fXxBU0RGR0hKS0w6WlhDVkJOTTw+P9C50YTRj9GG0YvRh9GD0LLRgdC"
                + "60LDQvNC10L/QuNC90YDRgtCz0L7RjNGI0LvQsdGJ0LTRjtC30LYu0YXRjdGKXNCZ0KTQr9Cm0KvQp9Cj0JLQodCa0JDQnNCV0J/"
                + "QmNCd0KDQotCT0J7QrNCo0JvQkdCp0JTQrtCX0JYs0KXQrdCqLw0JDCcICg==";

        assertEquals(helloWorldEncoded, java8Base64.internalEncode(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, java8Base64.internalEncode(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                java8Base64.internalEncode(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded, java8Base64.internalEncode(helloWorldDifferentCharsBytes));
    }

    @Test
    public void testEncodeUrlWithoutPadding() {
        final String helloWorldEncoded = "SGVsbG8gV29ybGQ";
        final String helloWorldTwoLinesEncoded = "SGVsbG8gV29ybGQNCk5ldyBMaW5lMg";
        final String helloWorldTwoLinesAndNewLineEncoded = "SGVsbG8gV29ybGQNClNlY29uZCBMaW5lDQo";
        final String helloWorldDifferentCharsEncoded = "YDEyMzQ1Njc4OTAtPX4hQCMkJV4mKigpXyvRkdCBIuKEljs6P3F3ZXJ0eXVpb3B"
                + "bXWFzZGZnaGprbDsnenhjdmJubSwuL1FXRVJUWVVJT1B7fXxBU0RGR0hKS0w6WlhDVkJOTTw-P9C50YTRj9GG0YvRh9GD0LLRgdC"
                + "60LDQvNC10L_QuNC90YDRgtCz0L7RjNGI0LvQsdGJ0LTRjtC30LYu0YXRjdGKXNCZ0KTQr9Cm0KvQp9Cj0JLQodCa0JDQnNCV0J_"
                + "QmNCd0KDQotCT0J7QrNCo0JvQkdCp0JTQrtCX0JYs0KXQrdCqLw0JDCcICg";

        assertEquals(helloWorldEncoded, java8Base64.internalEncodeUrlWithoutPadding(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, java8Base64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                java8Base64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded,
                java8Base64.internalEncodeUrlWithoutPadding(helloWorldDifferentCharsBytes));
    }

    @Test
    public void testDecodeMime() {
        final String helloWorldEncoded = "SGVsbG8gV29ybGQ=";
        final String helloWorldTwoLinesEncoded = "SGVsbG8gV29ybGQNCk5ldyBMaW5lMg==";
        final String helloWorldTwoLinesAndNewLineEncoded = "SGVsbG8gV29ybGQNClNlY29uZCBMaW5lDQo=";
        final String helloWorldDifferentCharsEncoded = "YDEyMzQ1Njc4OTAtPX4hQCMkJV4mKigpXyvRkdCBIuKEljs6P3F3ZXJ0eXVpb3B"
                + "bXWFzZGZnaGprbDsnenhjdmJubSwuL1FXRVJUWVVJT1B7fXxBU0RGR0hKS0w6WlhDVkJOTTw+P9C50YTRj9GG0YvRh9GD0LLRgdC"
                + "60LDQvNC10L/QuNC90YDRgtCz0L7RjNGI0LvQsdGJ0LTRjtC30LYu0YXRjdGKXNCZ0KTQr9Cm0KvQp9Cj0JLQodCa0JDQnNCV0J/"
                + "QmNCd0KDQotCT0J7QrNCo0JvQkdCp0JTQrtCX0JYs0KXQrdCqLw0JDCcICg==";

        assertArrayEquals(helloWorldBytes, java8Base64.internalDecodeMime(helloWorldEncoded));
        assertArrayEquals(helloWorldTwoLinesBytes, java8Base64.internalDecodeMime(helloWorldTwoLinesEncoded));
        assertArrayEquals(helloWorldTwoLinesAndNewLineBytes,
                java8Base64.internalDecodeMime(helloWorldTwoLinesAndNewLineEncoded));
        assertArrayEquals(helloWorldDifferentCharsBytes,
                java8Base64.internalDecodeMime(helloWorldDifferentCharsEncoded));
    }

}
