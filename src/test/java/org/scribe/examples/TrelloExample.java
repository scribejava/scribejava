package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TrelloApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

/**
 * Shows you how to use scribe with trello.com .
 *
 * @author Harald Wartig <hwartig@googlemail.com>
 */
public class TrelloExample
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.trello.com/1/members/me/boards";

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "";
    String apiSecret = "";

    Scanner in = new Scanner(System.in);

    if (apiKey.isEmpty() || apiSecret.isEmpty()) {
      System.out.println("=== Setup ===");
      System.out.println();

      System.out.println("We need your apiKey:");
      System.out.print(">>");
      apiKey = in.nextLine();

      System.out.println(".. and your apiSecret too:");
      System.out.print(">>");
      apiSecret = in.nextLine();
    }

    OAuthService service = new ServiceBuilder()
                                .provider(TrelloApi.class)
                                .apiKey(apiKey)
                                .apiSecret(apiSecret)
                                .build();

    System.out.println("=== Trello's OAuth Workflow ===");
    System.out.println();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");
    System.out.println();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(service.getAuthorizationUrl(requestToken));
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();

    // Trade the Request Token and Verifier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets take a look at your board list...");
    System.out.println();
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Go and build something awesome with Scribe & trello.com :)");
  }
}
