package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Layer7Api;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class Layer7Example
{
  private static final String PROTECTED_RESOURCE_URI = "http://preview.layer7tech.com:8080/protected/resource";

  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
        .provider(Layer7Api.SSL.class)
        .apiKey("Consumer")
        .apiSecret("Secret")
        .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== Layer7's OAuth Toolkit 1.0 Workflow ===");
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
    OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URI);
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe and Layer 7's OAuth Toolkit! :)");
    
  }
}