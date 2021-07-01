package com.github.scribejava.apis;

import com.github.scribejava.apis.esia.EsiaOAuthService;
import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;

import java.io.OutputStream;

public class EsiaApi extends DefaultApi20 {
  private static final String PROTOCOL = "https://";
  private static final String PRODUCTION_HOST = "esia.gosuslugi.ru";
  private static final String SANDBOX_HOST = "esia-portal1.test.gosuslugi.ru";
  private static final String AUTHORIZATION_PATH = "/aas/oauth2/ac";
  private static final String ACCESS_TOKEN_PATH = "/aas/oauth2/te";

  private final String host;

  protected EsiaApi(String host) {
    this.host = host;
  }

  private static class InstanceHolder {
    private static final EsiaApi PRODUCTION_INSTANCE = new EsiaApi(PRODUCTION_HOST);
    private static final EsiaApi SANDBOX_INSTANCE = new EsiaApi(SANDBOX_HOST);
  }

  public static EsiaApi production() {
    return InstanceHolder.PRODUCTION_INSTANCE;
  }

  public static EsiaApi sandbox() {
    return InstanceHolder.SANDBOX_INSTANCE;
  }

  @Override
  public String getAccessTokenEndpoint() {
    return PROTOCOL + host + ACCESS_TOKEN_PATH;
  }

  @Override
  protected String getAuthorizationBaseUrl() {
    return PROTOCOL + host + AUTHORIZATION_PATH;
  }

  @Override
  public EsiaOAuthService createService(String apiKey, String apiSecret, String callback, String defaultScope,
          String responseType, OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig,
          HttpClient httpClient) {
    return new EsiaOAuthService(this, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent,
            httpClientConfig, httpClient);
  }

  @Override
  public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
    return OpenIdJsonTokenExtractor.instance();
  }
}
