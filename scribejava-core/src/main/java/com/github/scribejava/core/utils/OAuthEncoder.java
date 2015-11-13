package com.github.scribejava.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;

/**
 * @author Pablo Fernandez
 */
public abstract class OAuthEncoder {

    private static final String CHARSET = "UTF-8";
    private static final Map<String, String> ENCODING_RULES;

    static {
        final Map<String, String> rules = new HashMap<>();
        rules.put("*", "%2A");
        rules.put("+", "%20");
        rules.put("%7E", "~");
        ENCODING_RULES = Collections.unmodifiableMap(rules);
    }

    public static String encode(final String plain) {
        Preconditions.checkNotNull(plain, "Cannot encode null object");
        String encoded = "";
        try {
            encoded = URLEncoder.encode(plain, CHARSET);
        } catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Charset not found while encoding string: " + CHARSET, uee);
        }
        for (final Map.Entry<String, String> rule : ENCODING_RULES.entrySet()) {
            encoded = applyRule(encoded, rule.getKey(), rule.getValue());
        }
        return encoded;
    }

    private static String applyRule(final String encoded, final String toReplace, final String replacement) {
        return encoded.replaceAll(Pattern.quote(toReplace), replacement);
    }

    public static String decode(final String encoded) {
        Preconditions.checkNotNull(encoded, "Cannot decode null object");
        try {
            return URLDecoder.decode(encoded, CHARSET);
        } catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Charset not found while decoding string: " + CHARSET, uee);
        }
    }
}
