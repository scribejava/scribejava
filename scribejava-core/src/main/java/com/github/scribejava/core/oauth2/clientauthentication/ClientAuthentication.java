package com.github.scribejava.core.oauth2.clientauthentication;

import com.github.scribejava.core.model.OAuthRequest;

/**
 * Represents<br>
 * 2.3. Client Authentication<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3
 * <br>
 * just implement this interface to implement "2.3.2. Other Authentication Methods"<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3.2
 *
 */
public interface ClientAuthentication {

    void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret);
}
