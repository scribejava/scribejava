package org.scribe.examples;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi20;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.IOException;
import java.util.Scanner;

public class LinkedIn20Example
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,location,industry,distance,relation-to-viewer,current-share,num-connections,num-connections-capped,summary,specialties,proposal-comments,associations,honors,interests,positions,publications,patents,languages,skills,certifications,educations,three-current-positions,three-past-positions,num-recommenders,recommendations-received,phone-numbers,im-accounts,twitter-accounts,date-of-birth,main-address,member-url-resources,picture-url,site-standard-profile-request:(url),api-public-profile-request:(url),site-public-profile-request:(url),api-standard-profile-request,public-profile-url,email-address)?format=json";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "your api key";
    String apiSecret = "your api secret";

    OAuthService service = new ServiceBuilder()
                                .provider(LinkedInApi20.class)
                                .apiKey(apiKey)
                                .apiSecret(apiSecret)
                                .callback("http://localhost:8000/")
                                .scope("r_fullprofile rw_nus")
                                .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== LinkedIn's OAuth Workflow ===");
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
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }

}
