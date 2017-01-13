package com.github.scribejava.core.utils;

import java.util.Locale;
import java.util.regex.Pattern;
import com.github.scribejava.core.model.OAuthConstants;

/**
 * Utils for checking preconditions and invariants
 */
public abstract class Preconditions {

    private static final String DEFAULT_MESSAGE = "Received an invalid parameter";

    // scheme = alpha *( alpha | digit | "+" | "-" | "." )
    private static final String URL_REGEXP = "^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+";

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


    /**
     * Checks that a URL is valid
     *
     * @param url any string
     * @param errorMsg error message
     *
     * @deprecated will be just removed. not used in ScribeJava
     */
    @Deprecated
    public static void checkValidUrl(String url, String errorMsg) {
        checkEmptyString(url, errorMsg);
        check(isUrl(url), errorMsg);
    }

    /**
     * Checks that a URL is a valid OAuth callback
     *
     * @param url any string
     * @param errorMsg error message
     *
     * @deprecated will be just removed. not used in ScribeJava
     */
    @Deprecated
    public static void checkValidOAuthCallback(String url, String errorMsg) {
        checkEmptyString(url, errorMsg);
        if (url.toLowerCase(Locale.getDefault()).compareToIgnoreCase(OAuthConstants.OUT_OF_BAND) != 0) {
            check(isUrl(url), errorMsg);
        }
    }

    private static boolean isUrl(String url) {
        return Pattern.compile(URL_REGEXP).matcher(url).matches();
    }

    private static void check(boolean requirements, String error) {
        if (!requirements) {
            throw new IllegalArgumentException(hasText(error) ? error : DEFAULT_MESSAGE);
        }
    }

}
