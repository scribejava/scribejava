package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Layer7Api;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class Layer7debug
{
  private static final String PROTECTED_RESOURCE_URI = "https://cdryden-pc.l7tech.local:8443/protected/resource";

  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
        .provider(Layer7Api.SSL.class)
        .apiKey("acf89db2-994e-427b-ac2c-88e6101f9433")
        .apiSecret("74d5e0db-cd8b-4d8e-a989-95a0746c3343")
        .debug()
        .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== Layer7's OAuth Toolkit 1.0 Workflow ===");
    System.out.println();
    
    // Obtain the Request Token
   // System.out.println("Fetching the Request Token...");
   // Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");
    System.out.println();

  //  System.out.println("Now go and authorize Scribe here:");
  //  System.out.println(service.getAuthorizationUrl(requestToken));
  //  System.out.println("And paste the verifier here");
    System.out.print(">>");
    //Verifier verifier = new Verifier(in.nextLine());
    System.out.println();

  //  Trade the Request Token and Verfier for the Access Token
  //  System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = new Token("1f709753-e37c-40c7-adb8-6493fa2240f3" , "bad1a821-1f70-456a-8f90-6e8f8808eec9"); 
        //service.getAccessToken(requestToken, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

  //  Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URI);
    request.addQuerystringParameter("Query", "Layer 7 Oauth");
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getBody());

    System.out.println();
    //  Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    request = new OAuthRequest(Verb.POST, "https://cdryden-pc.l7tech.local:8443/protected/resource");
    //request.addQuerystringParameter("Query", "Layer 7 Oauth");
    service.signRequest(accessToken, request);
    response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getBody());
    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe and Layer 7's OAuth Toolkit! :)");
  }
}