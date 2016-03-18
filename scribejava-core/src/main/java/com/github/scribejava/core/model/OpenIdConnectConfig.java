package com.github.scribejava.core.model;

import static com.github.scribejava.core.model.OpenIdConnectConstants.KEY_AUTHORIZATION_ENDPOINT;
import static com.github.scribejava.core.model.OpenIdConnectConstants.KEY_ISSUER;
import static com.github.scribejava.core.model.OpenIdConnectConstants.KEY_JWKS_URI;
import static com.github.scribejava.core.model.OpenIdConnectConstants.KEY_SCOPES_SUPPORTED;
import static com.github.scribejava.core.model.OpenIdConnectConstants.KEY_TOKEN_ENDPOINT;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class OpenIdConnectConfig {
    /**
     * This is a very crude, but simple, regex capable of parsing a JSON structure that does contain simple key/values
     * or keys with array values.
     */
    private static final Pattern JSON_REGEX = Pattern
            .compile("(?=\"([^\"]+)\"\\s*:\\s*(true|false|null|\"[^\"]+\"|\\[[^\\]]*\\]))");

    private final Map<String, String[]> config;

    public OpenIdConnectConfig(Map<String, String[]> config) {
        assertEntryPresent(config, KEY_AUTHORIZATION_ENDPOINT);
        assertEntryPresent(config, KEY_SCOPES_SUPPORTED);
        assertEntryPresent(config, KEY_TOKEN_ENDPOINT);
        assertEntryPresent(config, KEY_JWKS_URI);
        assertEntryPresent(config, KEY_ISSUER);
        this.config = new HashMap<>(config);
    }

    public static OpenIdConnectConfig readFromURL(URL url) throws IOException {
        return readFromURL(url, Proxy.NO_PROXY);
    }

    public static OpenIdConnectConfig readFromURL(URL url, Proxy proxy) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
        try {
            return read(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }

    public static OpenIdConnectConfig read(InputStream is) throws IOException {
        try (Scanner s = new Scanner(is, "UTF-8")) {
            s.useDelimiter(JSON_REGEX);
            if (!s.hasNext()) {
                throw new IOException("Unable to read configuration from stream: no/invalid content returned?!");
            }

            final Map<String, String[]> config = new HashMap<>();
            while (s.hasNext()) {
                final String in = s.next().trim();
                if ("{".equals(in) || "}".equals(in) || "".equals(in)) {
                    continue;
                }

                final Token token = new Token(in);
                config.put(token.key, token.value);
            }
            return new OpenIdConnectConfig(config);
        }
    }

    private static void assertEntryPresent(Map<String, ?> map, String key) {
        if (map.get(key) == null) {
            throw new IllegalArgumentException("No configuration entry found for '" + key + "'!");
        }
    }

    /**
     * @return the authorization endpoint that can be used to start the authorization, never <code>null</code>.
     */
    public String getAuthorizationEndpoint() {
        return getFirst(KEY_AUTHORIZATION_ENDPOINT);
    }

    /**
     * @return the token endpoint that can be used to obtain an access token, never <code>null</code>.
     */
    public String getTokenEndpoint() {
        return getFirst(KEY_TOKEN_ENDPOINT);
    }

    /**
     * @return the scopes supported by the OpenID connect provider, never <code>null</code>.
     */
    public String[] getSupportedScopes() {
        return get(KEY_SCOPES_SUPPORTED);
    }

    /**
     * @param key the key to retrieve the values for, cannot be <code>null</code>.
     * @return the value(s) (as array) associated with the given key. Can be <code>null</code> in case no value was
     *         associated to the given key.
     */
    public String[] get(String key) {
        return config.get(key);
    }

    /**
     * @param key the key to retrieve the first value for, cannot be <code>null</code>.
     * @return the first value associated with the given key. If no value is associated to the given key,
     *         <code>null</code> is returned.
     */
    public String getFirst(String key) {
        final String[] val = config.get(key);
        if (val == null || val.length == 0) {
            return null;
        }
        return val[0];
    }

    private static class Token {
        private final String key;
        private final String[] value;

        Token(String input) {
            Objects.requireNonNull(input);
            if (input.endsWith(",")) {
                input = input.substring(0, input.length() - 1);
            }

            final String[] entry = input.split("\\s*:\\s*", 2);
            this.key = unquote(entry[0]);

            String val = entry[1].trim();
            if (val.startsWith("[") && val.endsWith("]")) {
                val = val.substring(1, val.length() - 1);
                this.value = val.split("\\s*,\\s*");
            } else {
                this.value = new String[] { val };
            }
            for (int i = 0; i < this.value.length; i++) {
                this.value[i] = unquote(this.value[i]);
            }
        }

        static String unquote(String input) {
            input = input.trim();
            if (input.startsWith("\"") && input.endsWith("\"")) {
                return input.substring(1, input.length() - 1);
            }
            return input;
        }
    }
}
