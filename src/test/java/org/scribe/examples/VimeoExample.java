package org.scribe.examples;

import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class VimeoExample 
{
  private static final String NETWORK_NAME = "Vimeo";
  // replace with your API secret
  private static final String API_SECRET = "47e0d78390ea8191";
  // replace with your API key
  private static final String APIKEY = "9fa1ef0b08fba9a75f7614a9ebf856ce";
  private final static String VIMEO_BASE = "http://vimeo.com/api/rest/v2?method=%s";
  // vimeo.test.login resource requires oauth authentication
  private final static String VIMEO_METHOD = "vimeo.test.login";

  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
                                  .provider(VimeoApi.class)
                                  .apiKey(APIKEY) // replace with your API key
                                  .apiSecret(API_SECRET) // replace with your API secret
                                  .signatureType(SignatureType.QueryString)
                                  .build();
    Scanner in = new Scanner(System.in);
    
    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");

    String authorizationUrl = service.getAuthorizationUrl(requestToken);

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(authorizationUrl);
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);		
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    String protectedResourceURL = String.format(VIMEO_BASE, VIMEO_METHOD);
    OAuthRequest orequest = new OAuthRequest(Verb.GET, protectedResourceURL);
    service.signRequest(accessToken, orequest);
    Response oresponse = orequest.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println(oresponse.getCode());
    System.out.println(oresponse.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }
}
