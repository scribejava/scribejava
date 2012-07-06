package org.scribe.examples;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class FacebookAsyncExample
{
  private static final String NETWORK_NAME = "Facebook";
  private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args) throws Exception
  {
    // Replace these with your own api key and secret
    String apiKey = "your_app_id";
    String apiSecret = "your_api_secret";
    final OAuthServiceAsync service = new ServiceBuilder()
                                            .provider(FacebookApi.class)
                                            .apiKey(apiKey)
                                            .apiSecret(apiSecret)
                                            .callback("http://www.example.com/oauth_callback/")
                                            .buildAsync();
    Scanner in = new Scanner(System.in);

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    final CountDownLatch countDownLatch = new CountDownLatch(1);

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
    service.getAccessToken(EMPTY_TOKEN, verifier, new OAuthServiceAsync.AccessTokenCallback()
    {
      public void onAccessToken(Token accessToken)
      {
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

        countDownLatch.countDown();
      }

      public void onError(OAuthException authException)
      {
        authException.printStackTrace();
        countDownLatch.countDown();
      }
    });

    // Wait for all the asynchronous stuff to finish
    System.out.println("Sent and waiting for asynchronous activity to complete...");
    countDownLatch.await();

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

  }
}
