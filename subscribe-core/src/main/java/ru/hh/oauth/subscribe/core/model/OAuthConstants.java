/*
 Copyright 2010 Pablo Fernandez

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package ru.hh.oauth.subscribe.core.model;

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
