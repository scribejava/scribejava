package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Parameter;
import com.google.gson.Gson;
import com.ning.http.client.ProxyServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 */
class OAuth20ServiceUnit extends OAuth20Service {

  final static String token = "ae82980abab675c646a070686d5558ad";
  final static String state = "123";
  final static String expires = "3600";

  public OAuth20ServiceUnit(DefaultApi20 api, OAuthConfig config) {
    super(api, config);
  }

  protected Future<OAuth2AccessToken> getAccessTokenAsync(OAuthRequestAsync request,
      OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {

      final Gson json = new Gson();
      final Map<String, String> response = new HashMap<>();

      response.put(OAuthConstants.ACCESS_TOKEN, token);
      response.put(OAuthConstants.STATE, state);
      response.put("expires_in", expires);

      response.putAll( request.getHeaders() );
      response.putAll( request.getOauthParameters() );

      for(Parameter p : request.getQueryStringParams().getParams()) {
        response.put( "query-" + p.getKey(), p.getValue() );
      }

      final OAuth2AccessToken accessToken = new OAuth2AccessToken(token, json.toJson( response ));

      try {
          return new CompletedFuture(accessToken);
      }
      finally {
          if( callback != null ) {
              callback.onCompleted(accessToken);
          }
      }
  }
}
