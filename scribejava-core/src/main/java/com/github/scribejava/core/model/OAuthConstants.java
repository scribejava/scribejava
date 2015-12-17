package com.github.scribejava.core.model;

/**
 * This class contains OAuth constants, used project-wide
 *
 * @author Pablo Fernandez
 */
public interface OAuthConstants {

    String TIMESTAMP = "oauth_timestamp";
    String SIGN_METHOD = "oauth_signature_method";
    String SIGNATURE = "oauth_signature";
    String CONSUMER_SECRET = "oauth_consumer_secret";
    String CONSUMER_KEY = "oauth_consumer_key";
    String CALLBACK = "oauth_callback";
    String VERSION = "oauth_version";
    String NONCE = "oauth_nonce";
    String REALM = "realm";
    String PARAM_PREFIX = "oauth_";
    String TOKEN = "oauth_token";
    String TOKEN_SECRET = "oauth_token_secret";
    String OUT_OF_BAND = "oob";
    String VERIFIER = "oauth_verifier";
    String HEADER = "Authorization";
    Token EMPTY_TOKEN = new Token("", "");
    String SCOPE = "scope";

    // OAuth 2.0
    String ACCESS_TOKEN = "access_token";
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String REDIRECT_URI = "redirect_uri";
    String CODE = "code";
    String GRANT_TYPE = "grant_type";
    String AUTHORIZATION_CODE = "authorization_code";
    String STATE = "state";
}
