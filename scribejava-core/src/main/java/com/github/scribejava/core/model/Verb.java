package com.github.scribejava.core.model;

/**
 * An enumeration containing the most common HTTP Verbs.
 */
public enum Verb {

    GET(false), POST(true), PUT(true), DELETE(false, true), HEAD(false), OPTIONS(false), TRACE(false), PATCH(true);

    private final boolean requiresBody;
    private final boolean permitBody;

    Verb(boolean requiresBody) {
        this(requiresBody, requiresBody);
    }

    Verb(boolean requiresBody, boolean permitBody) {
        if (requiresBody && !permitBody) {
            throw new IllegalArgumentException();
        }
        this.requiresBody = requiresBody;
        this.permitBody = permitBody;
    }

    public boolean isRequiresBody() {
        return requiresBody;
    }

    public boolean isPermitBody() {
        return permitBody;
    }
}
