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

    /**
     * @return value
     * @deprecated use {@link #getValue() } to get a lower-cased value as in reference (RFC7009), otherwise you can
     * continue using this method. Note, that returned value will be UPPER-cased (not overrided toString method) in the
     * next release.
     */
    @Override
    @Deprecated
    public String toString() {
        return value;
    }
}
