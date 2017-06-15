package com.github.scribejava.core.model;

/**
 * An enumeration containing the most common HTTP Verbs.
 */
public enum Verb {

    GET(false), POST(true), PUT(true), DELETE(true), HEAD(false), OPTIONS(false), TRACE(false), PATCH(true);

    private final boolean permitBody;

    Verb(boolean permitBody) {
        this.permitBody = permitBody;
    }

    public boolean isPermitBody() {
        return permitBody;
    }
}
