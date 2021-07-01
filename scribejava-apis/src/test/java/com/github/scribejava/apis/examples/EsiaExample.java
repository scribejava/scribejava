package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.EsiaApi;
import com.github.scribejava.apis.esia.ECGOST2012SigningService;
import com.github.scribejava.apis.esia.EsiaJWTHeader;
import com.github.scribejava.apis.esia.EsiaJWTPayload;
import com.github.scribejava.apis.esia.EsiaOAuthService;
import com.github.scribejava.apis.jwt.JsonWebToken;
import com.github.scribejava.apis.jwt.JsonWebTokenExtractor;
import com.github.scribejava.apis.openid.OpenIdOAuth2AccessToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class EsiaExample {
  private static final String NETWORK_NAME = "esia.gosuslugi.ru";

  private EsiaExample() {
  }

  @SuppressWarnings("PMD.SystemPrintln")
  public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
    // Replace these with your client id
    final String clientId = "HH_AUTHORIZATION";
    final OAuth20Service service = new ServiceBuilder(clientId)
        .defaultScope("openid") // replace with desired scope
        .callback("http://your.site.com/callback")
        .build(EsiaApi.sandbox());
    ((EsiaOAuthService) service).setSigningService(ECGOST2012SigningService.usingPemFiles(
        "/home/lmsboris/esia/cert.pem", "/home/lmsboris/esia/key.pem"));

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    // Obtain the Authorization URL
    System.out.println("Fetching the Authorization URL...");
    final String customScope = "openid";
    final String authorizationUrl = service.getAuthorizationUrl();
    System.out.println("Got the Authorization URL!");
    System.out.println("Now go and authorize ScribeJava here:");
    System.out.println(authorizationUrl);

    System.out.println("And paste URL you got redirected to here");
    System.out.print(">>");
    System.out.println();
    final Scanner in = new Scanner(System.in);
    final String error = in.findInLine("[?&]error_description=([^&]+)");
    if (error != null) {
      System.err.println("Cannot get authorization code, error is " + OAuthEncoder.decode(error.substring(19)));
      return;
    }
    String code = in.findInLine("[?&]code=([^&]+)");
    if (code == null) {
      System.err.println("Cannot find authorization code parameter in this URL");
      return;
    }
    code = code.substring(6);
    System.out.println("Got code " + code);

    // Now trade authorization code for access token
    System.out.println("Trading the Authorization Code for an Access Token...");
    final OAuth2AccessToken accessToken = service.getAccessToken(AccessTokenRequestParams.create(code)
        .scope(customScope));
    System.out.println("Got the Access Token!");
    System.out.println("The raw response looks like this: " + accessToken.getRawResponse());
    System.out.println("OpenID JWT token looks like this: " + ((OpenIdOAuth2AccessToken) accessToken).getOpenIdToken());
    final JsonWebToken<EsiaJWTHeader, EsiaJWTPayload> jsonWebToken =
            JsonWebTokenExtractor.custom(EsiaJWTHeader.class, EsiaJWTPayload.class)
                    .extract(((OpenIdOAuth2AccessToken) accessToken).getOpenIdToken());
    System.out.println("OpenID JWT token parsed: " + jsonWebToken);
    System.out.println("ESIA user ID (OID) can be found in payload.subject='" + jsonWebToken.getPayload().subject
        + "' or payload.esiaSubject.oid=" + jsonWebToken.getPayload().esiaSubject.oid);

    System.out.println("Accessing protected resource (that is, service.signRequest(...) has not been implemented yet");
  }
}
