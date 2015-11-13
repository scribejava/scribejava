package com.github.scribejava.core.utils;

import java.util.Locale;
import java.util.regex.Pattern;
import com.github.scribejava.core.model.OAuthConstants;

/**
 * Utils for checking preconditions and invariants
 *
 * @author Pablo Fernandez
 */
public abstract class Preconditions {

    private static final String DEFAULT_MESSAGE = "Received an invalid parameter";

    // scheme = alpha *( alpha | digit | "+" | "-" | "." )
    private static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+");

    /**
     * Checks that an object is not null.
     *
     * @param object any object
     * @param errorMsg error message
     *
     * @throws IllegalArgumentException if the object is null
     */
    public static void checkNotNull(final Object object, final String errorMsg) {
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
    public static void checkEmptyString(final String string, final String errorMsg) {
        check(string != null && !string.trim().isEmpty(), errorMsg);
    }

    /**
     * Checks that a URL is valid
     *
     * @param url any string
     * @param errorMsg error message
     */
    public static void checkValidUrl(final String url, final String errorMsg) {
        checkEmptyString(url, errorMsg);
        check(isUrl(url), errorMsg);
    }

    /**
     * Checks that a URL is a valid OAuth callback
     *
     * @param url any string
     * @param errorMsg error message
     */
    public static void checkValidOAuthCallback(final String url, final String errorMsg) {
        checkEmptyString(url, errorMsg);
        if (url.toLowerCase(Locale.getDefault()).compareToIgnoreCase(OAuthConstants.OUT_OF_BAND) != 0) {
            check(isUrl(url), errorMsg);
        }
    }

    private static boolean isUrl(final String url) {
        return URL_PATTERN.matcher(url).matches();
    }

    private static void check(final boolean requirements, final String error) {
        if (!requirements) {
            throw new IllegalArgumentException(error == null || error.trim().length() <= 0 ? DEFAULT_MESSAGE : error);
        }
    }

}
