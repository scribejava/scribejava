package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class AWeberExample
{
	
  //To get your consumer key/secret, and view API docs, see https://labs.aweber.com/docs	
  private static final String ACCOUNT_RESOURCE_URL = "https://api.aweber.com/1.0/accounts/";

  private static final String CONSUMER_KEY = "";
  private static final String CONSUMER_SECRET = "";

  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
                                .provider(AWeberApi.class)
                                .apiKey(CONSUMER_KEY)
                                .apiSecret(CONSUMER_SECRET)
                                .build();
 
    Scanner in = new Scanner(System.in);

    System.out.println("=== AWeber's OAuth Workflow ===");
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

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, ACCOUNT_RESOURCE_URL);
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with AWeber and Scribe! :)");
  }

}