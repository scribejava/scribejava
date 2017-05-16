package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoToMeetingApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class GoToMeetingExample
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.citrixonline.com/G2M/rest/meetings";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "secret key";
    String apiSecret = "not used by GoToMeeting, can be anything";
    OAuthService service = new ServiceBuilder()
                                  .provider(GoToMeetingApi.class)
                                  .apiKey(apiKey)
                                  .apiSecret(apiSecret)
                                  .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== GoToMeetings's OAuth Workflow ===");
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
    System.out.println("That's it man! Go and build something awesome with Scribe! :)");

  }
}