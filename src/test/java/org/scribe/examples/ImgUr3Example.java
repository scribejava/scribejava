package org.scribe.examples;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import java.util.*;

public class ImgUr3Example
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.imgur.com/3/account/me/albums";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret (you'll need an read/write api key)
    String apiKey = "your_app_id";
    String apiSecret = "your_api_secret";
    OAuthService service = new ServiceBuilder().provider(ImgUr3Api.class).apiKey(apiKey).apiSecret(apiSecret).build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== ImgUr's OAuth Workflow ===");
    System.out.println();

    System.out.println("Now go and authorize Scribe here:");
    String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
    System.out.println(authorizationUrl);
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();

    // Trade the Request Token and Verfier for the Access Token
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
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }
}
