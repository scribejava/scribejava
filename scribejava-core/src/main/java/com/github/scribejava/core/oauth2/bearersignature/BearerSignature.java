package com.github.scribejava.core.oauth2.bearersignature;

import com.github.scribejava.core.model.OAuthRequest;

/**
 * Represents<br>
 * 2. Authenticated Requests<br>
 * https://tools.ietf.org/html/rfc6750#section-2
 */
public interface BearerSignature {
    void signRequest(String accessToken, OAuthRequest request);
}
