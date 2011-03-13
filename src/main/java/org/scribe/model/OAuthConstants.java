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
package org.scribe.model;

/**
 * This class contains OAuth constants, used project-wide
 * 
 * @author Pablo Fernandez
 */
public class OAuthConstants
{
  public static final String TIMESTAMP = "oauth_timestamp";
  public static final String SIGN_METHOD = "oauth_signature_method";
  public static final String SIGNATURE = "oauth_signature";
  public static final String CONSUMER_SECRET = "oauth_consumer_secret";
  public static final String CONSUMER_KEY = "oauth_consumer_key";
  public static final String CALLBACK = "oauth_callback";
  public static final String VERSION = "oauth_version";
  public static final String NONCE = "oauth_nonce";
  public static final String PARAM_PREFIX = "oauth_";
  public static final String TOKEN = "oauth_token";
  public static final String TOKEN_SECRET = "oauth_token_secret";
  public static final String OUT_OF_BAND = "oob";
  public static final String VERIFIER = "oauth_verifier";
  public static final String HEADER = "Authorization";
  public static final Token EMPTY_TOKEN = new Token("", "");
  public static final String SCOPE = "scope";

  //OAuth 2.0
  public static final String ACCESS_TOKEN = "access_token";
  public static final String CLIENT_ID = "client_id";
  public static final String CLIENT_SECRET = "client_secret";
  public static final String REDIRECT_URI = "redirect_uri";
  public static final String CODE = "code";
  
}
