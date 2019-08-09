package com.github.scribejava.core.model;

import java.io.Serializable;

/**
 * Represents an abstract OAuth (1 and 2) token (either request or access token)
 */
public abstract class Token implements Serializable {

    private static final long serialVersionUID = -8409640649946468092L;

    private final String rawResponse;

    protected Token(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public String getRawResponse() {
        if (rawResponse == null) {
            throw new IllegalStateException(
                    "This token object was not constructed by ScribeJava and does not have a rawResponse");
        }
        return rawResponse;
    }

    public String getParameter(String parameter) {
        String value = null;
        for (String str : rawResponse.split("&")) {
            if (str.startsWith(parameter + '=')) {
                final String[] part = str.split("=");
                if (part.length > 1) {
                    value = part[1].trim();
                }
                break;
            }
        }
        return value;
    }
}
