package org.scribe.examples;

import java.util.*;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class FreelancerExample
{

  private static final String NETWORK_NAME = "Freelancer";
  private static final String AUTHORIZE_URL = "http://www.sandbox.freelancer.com/users/api-token/auth.php?oauth_token=";
  private static final String PROTECTED_RESOURCE_URL = "http://api.sandbox.freelancer.com/Job/getJobList.json";
  private static final String SCOPE = "http://api.sandbox.freelancer.com";

  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
                                  .provider(FreelancerApi.Sandbox.class)
                                  .signatureType(SignatureType.QueryString)
                                  .apiKey("7f5a168a0bfdbd15b4a9ea2a969661c731cdea56")
                                  .apiSecret("7bb8961b94873802f1c5344f671a518e087f5785")
                                  .scope(SCOPE)
                                  .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");
    System.out.println("(if your curious it looks like this: " + requestToken + " )");
    System.out.println();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(AUTHORIZE_URL + requestToken.getToken());
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
    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
    service.signRequest(accessToken, request);
    request.addHeader("GData-Version", "3.0");
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }
}
