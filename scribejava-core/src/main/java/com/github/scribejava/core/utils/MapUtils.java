package com.github.scribejava.core.utils;

import java.util.Map;

/**
 * @author Pablo Fernandez
 */
public abstract class MapUtils {

    public static <K, V> String toString(final Map<K, V> map) {
        if (map == null) {
            return "";
        }
        if (map.isEmpty()) {
            return "{}";
        }

        final StringBuilder result = new StringBuilder();
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            result.append(String.format(", %s -> %s ", entry.getKey().toString(), entry.getValue().toString()));
        }
        return "{" + result.substring(1) + "}";
    }
}
