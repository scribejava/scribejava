package com.github.scribejava.core.utils;

import java.util.Map;

public abstract class MapUtils {

    public static <K, V> String toString(Map<K, V> map) {
        if (map == null) {
            return "";
        }
        if (map.isEmpty()) {
            return "{}";
        }

        final StringBuilder result = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result.append(", ")
                    .append(entry.getKey().toString())
                    .append(" -> ")
                    .append(entry.getValue().toString())
                    .append(' ');
        }
        return "{" + result.append('}').substring(1);
    }
}
