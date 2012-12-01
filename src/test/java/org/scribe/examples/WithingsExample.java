package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.WithingsApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class WithingsExample
{
  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "myKey";
    String apiSecret = "mySecret";
    OAuthService service = new ServiceBuilder()
                            .provider(WithingsApi.class)
                            .apiKey(apiKey).apiSecret(apiSecret)
                            .signatureType(SignatureType.QueryString)
                            .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== Withing's OAuth Workflow ===");
    System.out.println();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");
    System.out.println();

    // Obtain the Authorization URL
    System.out.println("Fetching the Authorization URL...");
    String authorizationUrl = service.getAuthorizationUrl(requestToken);
    System.out.println("Got the Authorization URL!");
    System.out.println("Now go and authorize Withings here:");
    System.out.println(authorizationUrl);
    System.out.println("And paste the authorization code here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println("And paste the user ID here");
    System.out.print(">>");
    String userId = in.nextLine();
    System.out.println();

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://wbsapi.withings.net/measure");
    request.addQuerystringParameter("userid", userId);
    request.addQuerystringParameter("action", "getmeas");
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());
  }
}