package com.github.scribejava.core.utils;

/**
 * Utils for checking preconditions and invariants
 */
public abstract class Preconditions {

    private static final String DEFAULT_MESSAGE = "Received an invalid parameter";

    /**
     * Checks that an object is not null.
     *
     * @param object any object
     * @param errorMsg error message
     *
     * @throws IllegalArgumentException if the object is null
     */
    public static void checkNotNull(Object object, String errorMsg) {
        check(object != null, errorMsg);
    }

    /**
     * Checks that a string is not null or empty
     *
     * @param string any string
     * @param errorMsg error message
     *
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void checkEmptyString(String string, String errorMsg) {
        check(hasText(string), errorMsg);
    }

    public static boolean hasText(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        final int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static void check(boolean requirements, String error) {
        if (!requirements) {
            throw new IllegalArgumentException(hasText(error) ? error : DEFAULT_MESSAGE);
        }
    }

}
