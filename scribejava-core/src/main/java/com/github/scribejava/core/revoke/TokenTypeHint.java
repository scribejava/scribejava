package com.github.scribejava.core.revoke;

/**
 *
 * as stated in RFC 7009 <br>
 * 2.1. Revocation Request
 *
 * @see <a href="https://tools.ietf.org/html/rfc7009#section-2.1">RFC 7009, 2.1. Revocation Request</a>
 */
public enum TokenTypeHint {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");

    private final String value;

    TokenTypeHint(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
