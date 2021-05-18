package com.github.scribejava.core.base64;

import java.io.UnsupportedEncodingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class Base64Test {

    private Base64 java8Base64;
    private Base64 commonsCodecBase64;
    private Base64 jaxbBase64;
    private Base64 jaxb230Base64;
    private byte[] helloWorldBytes;
    private byte[] helloWorldTwoLinesBytes;
    private byte[] helloWorldTwoLinesAndNewLineBytes;
    private byte[] helloWorldDifferentCharsBytes;
    private byte[] bytes;
    private byte[] allBytes;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        helloWorldBytes = "Hello World".getBytes("UTF-8");
        helloWorldTwoLinesBytes = "Hello World\r\nNew Line2".getBytes("UTF-8");
        helloWorldTwoLinesAndNewLineBytes = "Hello World\r\nSecond Line\r\n".getBytes("UTF-8");
        helloWorldDifferentCharsBytes = ("`1234567890-=~!@#$%^&*()_+ёЁ\"№;:?qwertyuiop[]asdfghjkl;'zxcvbnm,./QWERTYUIOP"
                + "{}|ASDFGHJKL:ZXCVBNM<>?йфяцычувскамепинртгоьшлбщдюзж.хэъ\\ЙФЯЦЫЧУВСКАМЕПИНРТГОЬШЛБЩДЮЗЖ,ХЭЪ/\r\t\f\'"
                + "\b\n").getBytes("UTF-8");
        bytes = new byte[]{48, -126, 2, 118, 2, 1, 0, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 4, -126,
            2, 96, 48, -126, 2, 92, 2, 1, 0, 2, -127, -127, 0, -61, -48, -28, 16, -116, -58, 85, 42, -39, 54, 50, -119,
            18, 40, 17, 75, 51, -24, 113, -109, 38, 17, -18, 106, -60, -74, -97, 29, 82, 123, -128, -88, -34, 92, 112,
            -57, 43, -101, 85, -47, 99, -16, 11, -95, 28, -46, 82, -104, -101, -29, -106, -106, -45, -80, 99, -93, 45,
            -102, 107, 31, 32, -60, 13, -46, 102, 127, 81, 94, -98, -56, 117, 50, 21, 39, 5, -98, 26, -18, -30, -21,
            102, -78, -77, 20, 113, -55, 117, -87, -105, -10, -100, 90, -92, 31, 61, -68, -73, -121, -108, 42, 45, -10,
            21, 87, 118, -74, 71, -100, -37, 96, -24, 87, 102, 68, -95, -1, -14, 6, -20, -14, 32, -14, 33, -84, -123,
            -65, 54, 3, 2, 3, 1, 0, 1, 2, -127, -128, 62, 115, -45, 41, 76, 28, -67, 113, 11, 17, -12, 16, 47, -112, 67,
            -29, -66, 76, 118, 92, -66, 25, -99, -10, -61, -126, -109, 64, -32, -37, -82, -17, 44, -20, 66, -77, -29,
            62, -119, -94, 92, -61, 100, -110, 32, 5, 28, 126, -69, -55, 92, 112, 2, 88, 17, -113, 43, -82, 66, 88, 13,
            53, 58, 74, -65, 36, 45, 93, -63, -15, 125, -7, -44, -45, -51, -76, 86, 97, 54, -36, -49, -117, -18, 56, 54,
            78, 80, 119, -6, -75, 39, 16, 57, -125, -68, 42, 50, -114, 92, 6, 13, 30, -91, 53, -66, -19, -20, 88, 32,
            -38, 36, 126, -119, -86, 47, -46, 37, 115, -49, -23, 125, -61, 75, 37, 70, 92, -122, -79, 2, 65, 0, -11,
            -105, 91, 105, -73, 54, 97, 96, -87, -16, -15, -73, 15, 31, -80, -96, -74, -53, -54, 53, -17, -9, 39, 62,
            58, 51, 68, 107, 86, 111, -62, -48, -125, 117, 66, 111, -55, 27, 56, 81, -50, 96, -47, -102, -50, -83, -52,
            -17, -20, 3, -42, -94, 11, 23, 104, 127, 29, -25, 32, 43, -41, -112, -83, -99, 2, 65, 0, -52, 29, 122, 9,
            49, -14, -118, 110, -79, 107, 76, -88, 4, -49, 40, 32, 59, 88, 45, -71, 62, 78, 93, -121, -123, 123, 3, 4,
            111, -112, 27, 12, -115, -123, 125, 39, 54, 96, -2, -46, 30, 40, -4, -119, 13, -121, 118, -23, 1, -83, -76,
            -26, -117, -86, -79, -121, 113, -26, 33, 30, 124, 35, -16, 31, 2, 65, 0, -47, -113, 111, -81, 75, 104, -103,
            -69, 20, 7, -57, 25, -65, 75, -7, 57, -118, 1, 102, -16, -109, 108, -64, 13, -73, 55, -37, -32, 3, -121,
            -90, 34, -86, -87, -70, 33, 12, -25, -81, 45, 14, -1, 74, -101, -32, 84, 41, -107, 104, 60, -10, 62, -101,
            92, 68, 12, -124, 5, -98, 76, 10, -53, 39, 121, 2, 64, 7, 106, 102, -67, -96, -57, -20, 9, -101, 126, -121,
            121, 111, 59, 75, 124, -24, 75, 10, -42, 57, 18, 69, -55, -97, -86, -39, 112, 54, -47, 104, 122, 43, 70, 23,
            70, -18, 109, -43, -76, 50, -114, 80, -90, 118, 12, 94, -32, -106, 68, 6, 87, 125, -23, -124, -85, -92, 18,
            -75, 79, 83, 57, 71, 7, 2, 64, 73, -64, -3, 78, -90, -122, -64, -99, -29, -71, 75, 21, -74, -24, -43, -37,
            116, -89, 31, -115, -30, 50, 8, 23, 79, -71, -68, -39, 36, -23, 60, 102, -90, -42, 19, -33, -102, -85, -74,
            103, 73, -30, 120, -15, 104, -9, 110, -24, -127, 14, -57, -44, 67, 9, 80, 120, 42, 94, 107, -81, -109, 101,
            -1, 91};

        allBytes = new byte[]{-128, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118, -117, -116, -115, -114,
            -113, -112, -111, -110, -109, -108, -107, -106, -105, -104, -103, -102, -101, -100, -99, -98, -97, -96, -95,
            -94, -93, -92, -91, -90, -89, -88, -87, -86, -85, -84, -83, -82, -81, -80, -79, -78, -77, -76, -75, -74,
            -73, -72, -71, -70, -69, -68, -67, -66, -65, -64, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53,
            -52, -51, -50, -49, -48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33, -32,
            -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11,
            -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
            46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
            73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99,
            100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
            121, 122, 123, 124, 125, 126, 127};

        java8Base64 = new Java8Base64();
        commonsCodecBase64 = new CommonsCodecBase64();
        jaxbBase64 = new JaxbBase64();
        jaxb230Base64 = new Jaxb230Base64();
    }

    @Test
    public void allImplementationsAreAvailable() {
        assertTrue(Java8Base64.isAvailable());
        assertTrue(CommonsCodecBase64.isAvailable());
        assertTrue(JaxbBase64.isAvailable());
        assertTrue(Jaxb230Base64.isAvailable());
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
        final String str = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPQ5BCMxlUq2TYy"
                + "iRIoEUsz6HGTJhHuasS2nx1Se4Co3lxwxyubVdFj8AuhHNJSmJvjlpbTsGOjLZpr"
                + "HyDEDdJmf1Fensh1MhUnBZ4a7uLrZrKzFHHJdamX9pxapB89vLeHlCot9hVXdrZH"
                + "nNtg6FdmRKH/8gbs8iDyIayFvzYDAgMBAAECgYA+c9MpTBy9cQsR9BAvkEPjvkx2"
                + "XL4ZnfbDgpNA4Nuu7yzsQrPjPomiXMNkkiAFHH67yVxwAlgRjyuuQlgNNTpKvyQt"
                + "XcHxffnU0820VmE23M+L7jg2TlB3+rUnEDmDvCoyjlwGDR6lNb7t7Fgg2iR+iaov"
                + "0iVzz+l9w0slRlyGsQJBAPWXW2m3NmFgqfDxtw8fsKC2y8o17/cnPjozRGtWb8LQ"
                + "g3VCb8kbOFHOYNGazq3M7+wD1qILF2h/HecgK9eQrZ0CQQDMHXoJMfKKbrFrTKgE"
                + "zyggO1gtuT5OXYeFewMEb5AbDI2FfSc2YP7SHij8iQ2HdukBrbTmi6qxh3HmIR58"
                + "I/AfAkEA0Y9vr0tombsUB8cZv0v5OYoBZvCTbMANtzfb4AOHpiKqqbohDOevLQ7/"
                + "SpvgVCmVaDz2PptcRAyEBZ5MCssneQJAB2pmvaDH7Ambfod5bztLfOhLCtY5EkXJ"
                + "n6rZcDbRaHorRhdG7m3VtDKOUKZ2DF7glkQGV33phKukErVPUzlHBwJAScD9TqaG"
                + "wJ3juUsVtujV23SnH43iMggXT7m82STpPGam1hPfmqu2Z0niePFo927ogQ7H1EMJ"
                + "UHgqXmuvk2X/Ww==";

        final String allBytesStr = "gIGCg4SFhoeIiYqLjI2Oj5CRkpOUlZaXmJmam5ydnp+goaKjpKWmp6ipqqusra6vsLGys7S1tre4ubq7vL2"
                + "+v8DBwsPExcbHyMnKy8zNzs/Q0dLT1NXW19jZ2tvc3d7f4OHi4+Tl5ufo6err7O3u7/Dx8vP09fb3+Pn6+/z9/v8AAQIDBAUGBwg"
                + "JCgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5Ojs8PT4/QEFCQ0RFRkdISUpLTE1OT1BRUlN"
                + "UVVZXWFlaW1xdXl9gYWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXp7fH1+fw==";

        assertEquals(helloWorldEncoded, java8Base64.internalEncode(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, java8Base64.internalEncode(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                java8Base64.internalEncode(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded, java8Base64.internalEncode(helloWorldDifferentCharsBytes));
        assertEquals(str, java8Base64.internalEncode(bytes));
        assertEquals(allBytesStr, java8Base64.internalEncode(allBytes));

        assertEquals(helloWorldEncoded, commonsCodecBase64.internalEncode(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, commonsCodecBase64.internalEncode(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                commonsCodecBase64.internalEncode(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded, commonsCodecBase64.internalEncode(helloWorldDifferentCharsBytes));
        assertEquals(str, commonsCodecBase64.internalEncode(bytes));
        assertEquals(allBytesStr, commonsCodecBase64.internalEncode(allBytes));

        assertEquals(helloWorldEncoded, jaxbBase64.internalEncode(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, jaxbBase64.internalEncode(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded, jaxbBase64.internalEncode(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded, jaxbBase64.internalEncode(helloWorldDifferentCharsBytes));
        assertEquals(str, jaxbBase64.internalEncode(bytes));
        assertEquals(allBytesStr, jaxbBase64.internalEncode(allBytes));

        assertEquals(helloWorldEncoded, jaxb230Base64.internalEncode(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, jaxb230Base64.internalEncode(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                jaxb230Base64.internalEncode(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded, jaxb230Base64.internalEncode(helloWorldDifferentCharsBytes));
        assertEquals(str, jaxb230Base64.internalEncode(bytes));
        assertEquals(allBytesStr, jaxb230Base64.internalEncode(allBytes));
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
        final String str = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPQ5BCMxlUq2TYy"
                + "iRIoEUsz6HGTJhHuasS2nx1Se4Co3lxwxyubVdFj8AuhHNJSmJvjlpbTsGOjLZpr"
                + "HyDEDdJmf1Fensh1MhUnBZ4a7uLrZrKzFHHJdamX9pxapB89vLeHlCot9hVXdrZH"
                + "nNtg6FdmRKH_8gbs8iDyIayFvzYDAgMBAAECgYA-c9MpTBy9cQsR9BAvkEPjvkx2"
                + "XL4ZnfbDgpNA4Nuu7yzsQrPjPomiXMNkkiAFHH67yVxwAlgRjyuuQlgNNTpKvyQt"
                + "XcHxffnU0820VmE23M-L7jg2TlB3-rUnEDmDvCoyjlwGDR6lNb7t7Fgg2iR-iaov"
                + "0iVzz-l9w0slRlyGsQJBAPWXW2m3NmFgqfDxtw8fsKC2y8o17_cnPjozRGtWb8LQ"
                + "g3VCb8kbOFHOYNGazq3M7-wD1qILF2h_HecgK9eQrZ0CQQDMHXoJMfKKbrFrTKgE"
                + "zyggO1gtuT5OXYeFewMEb5AbDI2FfSc2YP7SHij8iQ2HdukBrbTmi6qxh3HmIR58"
                + "I_AfAkEA0Y9vr0tombsUB8cZv0v5OYoBZvCTbMANtzfb4AOHpiKqqbohDOevLQ7_"
                + "SpvgVCmVaDz2PptcRAyEBZ5MCssneQJAB2pmvaDH7Ambfod5bztLfOhLCtY5EkXJ"
                + "n6rZcDbRaHorRhdG7m3VtDKOUKZ2DF7glkQGV33phKukErVPUzlHBwJAScD9TqaG"
                + "wJ3juUsVtujV23SnH43iMggXT7m82STpPGam1hPfmqu2Z0niePFo927ogQ7H1EMJ"
                + "UHgqXmuvk2X_Ww";

        final String allBytesStr = "gIGCg4SFhoeIiYqLjI2Oj5CRkpOUlZaXmJmam5ydnp-goaKjpKWmp6ipqqusra6vsLGys7S1tre4ubq7vL2"
                + "-v8DBwsPExcbHyMnKy8zNzs_Q0dLT1NXW19jZ2tvc3d7f4OHi4-Tl5ufo6err7O3u7_Dx8vP09fb3-Pn6-_z9_v8AAQIDBAUGBwg"
                + "JCgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5Ojs8PT4_QEFCQ0RFRkdISUpLTE1OT1BRUlN"
                + "UVVZXWFlaW1xdXl9gYWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXp7fH1-fw";

        assertEquals(helloWorldEncoded, java8Base64.internalEncodeUrlWithoutPadding(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, java8Base64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                java8Base64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded,
                java8Base64.internalEncodeUrlWithoutPadding(helloWorldDifferentCharsBytes));
        assertEquals(str, java8Base64.internalEncodeUrlWithoutPadding(bytes));
        assertEquals(allBytesStr, java8Base64.internalEncodeUrlWithoutPadding(allBytes));

        assertEquals(helloWorldEncoded, commonsCodecBase64.internalEncodeUrlWithoutPadding(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded,
                commonsCodecBase64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                commonsCodecBase64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded,
                commonsCodecBase64.internalEncodeUrlWithoutPadding(helloWorldDifferentCharsBytes));
        assertEquals(str, commonsCodecBase64.internalEncodeUrlWithoutPadding(bytes));
        assertEquals(allBytesStr, commonsCodecBase64.internalEncodeUrlWithoutPadding(allBytes));

        assertEquals(helloWorldEncoded, jaxbBase64.internalEncodeUrlWithoutPadding(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, jaxbBase64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                jaxbBase64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded,
                jaxbBase64.internalEncodeUrlWithoutPadding(helloWorldDifferentCharsBytes));
        assertEquals(str, jaxbBase64.internalEncodeUrlWithoutPadding(bytes));
        assertEquals(allBytesStr, jaxbBase64.internalEncodeUrlWithoutPadding(allBytes));

        assertEquals(helloWorldEncoded, jaxb230Base64.internalEncodeUrlWithoutPadding(helloWorldBytes));
        assertEquals(helloWorldTwoLinesEncoded, jaxb230Base64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesBytes));
        assertEquals(helloWorldTwoLinesAndNewLineEncoded,
                jaxb230Base64.internalEncodeUrlWithoutPadding(helloWorldTwoLinesAndNewLineBytes));
        assertEquals(helloWorldDifferentCharsEncoded,
                jaxb230Base64.internalEncodeUrlWithoutPadding(helloWorldDifferentCharsBytes));
        assertEquals(str, jaxb230Base64.internalEncodeUrlWithoutPadding(bytes));
        assertEquals(allBytesStr, jaxb230Base64.internalEncodeUrlWithoutPadding(allBytes));
    }
}
