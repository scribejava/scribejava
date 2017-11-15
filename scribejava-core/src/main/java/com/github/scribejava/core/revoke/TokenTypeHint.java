package com.github.scribejava.core.revoke;

/**
 *
 * as stated in RFC 7009 <br>
 * 2.1. Revocation Request
 *
 * @see <a href="https://tools.ietf.org/html/rfc7009#section-2.1">RFC 7009, 2.1. Revocation Request</a>
 */
public enum TokenTypeHint {
    access_token, refresh_token
}
