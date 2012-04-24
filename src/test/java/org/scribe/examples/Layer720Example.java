package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class Layer720Example
{
  private static final String PROTECTED_RESOURCE_URL = "https://cdryden-pc.l7tech.local:9443/oauth/v2/protectedapi";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    System.setProperty("java.net.useSystemProxies", "true");
    // Replace these with your own api key and secret
    String apiKey = "54f0c455-4d80-421f-82ca-9194df24859d";
    String apiSecret = "a0f2742f-31c7-436f-9802-b7015b8fd8e6";
    OAuthService service = new ServiceBuilder()
                                  .provider(Layer7Api20.class)
                                  .apiKey(apiKey)
                                  .apiSecret(apiSecret)
                                  .callback("http://cdryden-pc.l7tech.local")
                                  .scope("oob")
                                  .debug()
                                  .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== Layer 7's OAuth 2.0 Workflow ===");
    System.out.println();

    // Obtain the Authorization URL
    System.out.println("Fetching the Authorization URL...");
    String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
    System.out.println("Got the Authorization URL!");
    System.out.println("Now go and authorize Scribe here:");
    System.out.println(authorizationUrl);
    System.out.println("And paste the authorization code here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();
    
    // Trade the Request Token and Verifier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
}}
