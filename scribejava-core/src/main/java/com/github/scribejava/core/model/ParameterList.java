package com.github.scribejava.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * @author Pablo Fernandez
 */
public class ParameterList {

    private static final char QUERY_STRING_SEPARATOR = '?';
    private static final String PARAM_SEPARATOR = "&";
    private static final String PAIR_SEPARATOR = "=";
    private static final String EMPTY_STRING = "";

    private final List<Parameter> params;

    public ParameterList() {
        params = new ArrayList<>();
    }

    ParameterList(final List<Parameter> params) {
        this.params = new ArrayList<>(params);
    }

    public ParameterList(final Map<String, String> map) {
        this();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            params.add(new Parameter(entry.getKey(), entry.getValue()));
        }
    }

    public void add(final String key, final String value) {
        params.add(new Parameter(key, value));
    }

    public String appendTo(String url) {
        Preconditions.checkNotNull(url, "Cannot append to null URL");
        final String queryString = asFormUrlEncodedString();
        if (queryString.equals(EMPTY_STRING)) {
            return url;
        } else {
            url += url.indexOf(QUERY_STRING_SEPARATOR) == -1 ? QUERY_STRING_SEPARATOR : PARAM_SEPARATOR;
            url += queryString;
            return url;
        }
    }

    public String asOauthBaseString() {
        return OAuthEncoder.encode(asFormUrlEncodedString());
    }

    public String asFormUrlEncodedString() {
        if (params.isEmpty()) {
            return EMPTY_STRING;
        }

        final StringBuilder builder = new StringBuilder();
        for (final Parameter p : params) {
            builder.append('&').append(p.asUrlEncodedPair());
        }
        return builder.toString().substring(1);
    }

    public void addAll(final ParameterList other) {
        params.addAll(other.getParams());
    }

    public void addQuerystring(final String queryString) {
        if (queryString != null && queryString.length() > 0) {
            for (final String param : queryString.split(PARAM_SEPARATOR)) {
                final String pair[] = param.split(PAIR_SEPARATOR);
                final String key = OAuthEncoder.decode(pair[0]);
                final String value = pair.length > 1 ? OAuthEncoder.decode(pair[1]) : EMPTY_STRING;
                params.add(new Parameter(key, value));
            }
        }
    }

    public boolean contains(final Parameter param) {
        return params.contains(param);
    }

    public int size() {
        return params.size();
    }

    public List<Parameter> getParams() {
        return params;
    }

    public ParameterList sort() {
        final ParameterList sorted = new ParameterList(params);
        Collections.sort(sorted.getParams());
        return sorted;
    }
}
