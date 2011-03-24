package org.scribe.utils;

/**
 * User: elwood
 * Date: 27.04.2010
 * Time: 12:32:15
 */
public class HexStringsConverter {
    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String toHexString(byte bytes[]) {
        if (bytes == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int iter = 0; iter < bytes.length; iter++) {
            byte high = (byte) ((bytes[iter] & 0xf0) >> 4);
            byte low = (byte) (bytes[iter] & 0x0f);
            sb.append(nibble2char(high));
            sb.append(nibble2char(low));
        }

        return sb.toString();
    }

    private static char nibble2char(byte b) {
        byte nibble = (byte) (b & 0x0f);
        if (nibble < 10) {
            return (char) ('0' + nibble);
        }
        return (char) ('a' + nibble - 10);
    }
}
