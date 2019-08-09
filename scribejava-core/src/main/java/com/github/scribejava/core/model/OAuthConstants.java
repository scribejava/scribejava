package com.github.scribejava.core.model;

/**
 * This class contains OAuth constants, used project-wide
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
    String VERIFIER = "oauth_verifier";
    String HEADER = "Authorization";
    String SCOPE = "scope";
    String BASIC = "Basic";

    // OAuth 1.0
    /**
     * to indicate an out-of-band configuration
     * @see <a href="https://tools.ietf.org/html/rfc5849#section-2.1">The OAuth 1.0 Protocol</a>
     */
    String OOB = "oob";

    // OAuth 2.0
    String ACCESS_TOKEN = "access_token";
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String REDIRECT_URI = "redirect_uri";
    String CODE = "code";
    String REFRESH_TOKEN = "refresh_token";
    String GRANT_TYPE = "grant_type";
    String AUTHORIZATION_CODE = "authorization_code";
    String CLIENT_CREDENTIALS = "client_credentials";
    String STATE = "state";
    String USERNAME = "username";
    String PASSWORD = "password";
    String RESPONSE_TYPE = "response_type";
    String RESPONSE_TYPE_CODE = "code";

    //not OAuth specific
    String USER_AGENT_HEADER_NAME = "User-Agent";
}
