package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi20;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class LinkedIn20ExampleWithScopes
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.linkedin.com/v1/people/~";

  private static final Token EMPTY_TOKEN = null;

  private static final String state = "unique_state";

  public static void main(String[] args) throws MalformedURLException {
    OAuthService service = new ServiceBuilder()
        .provider(LinkedInApi20.class)
        .apiKey("777qp50wsrnbes")
        .apiSecret("KRTIgIYdJIWB5I6z")
        .scope("r_basicprofile")
        .signatureType(SignatureType.Header)
        .state(state)
        .callback("http://127.0.0.1")
        .build();

    System.out.println("=== LinkedIn's OAuth Workflow ===");
    System.out.println();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(service.getAuthorizationUrl(EMPTY_TOKEN));
    System.out.println("And paste the callback URL here");
    System.out.print(">>");

    Scanner in = new Scanner(System.in);
    String input = in.nextLine();

    URL url = new URL(input);
    String query = url.getQuery();
    if (query == null) {
        System.out.println("Wrong callback request!");
        System.out.println(input);
        System.exit(0);
    }

    String[] pairs = query.split("&");
    if (pairs.length != 2) {
          System.out.println("Wrong amount of callback parameters!");
          System.out.println(input);
          System.exit(0);
    }

    String accessCode = pairs[0].split("=")[1];
    String callbackState = pairs[1].split("=")[1];

    if (!callbackState.equals(state)) {
          System.out.println("Wrong state! Could be an attempt of CSRF attack!");
    }

    System.out.println();

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(EMPTY_TOKEN, new Verifier(accessCode));

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
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it! Go and build something awesome with Scribe! :)");
  }

}
