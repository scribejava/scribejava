package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import org.scribe.oauth.OAuthServiceAsync;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class TwitterAsyncExample
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1/statuses/update.json";
  
  public static void main(String[] args) throws Exception
  {
    final OAuthServiceAsync service = new ServiceBuilder()
                                .provider(TwitterApi.class)
                                .apiKey("6icbcAXyZx67r8uTAUM5Qw")
                                .apiSecret("SCCAdUUc6LXxiazxH3N0QfpNUvlUy84mZ2XZKiv39s")
                                .buildAsync();

    System.out.println("=== Twitter's OAuth Workflow ===");
    System.out.println();

    final CountDownLatch countDownLatch = new CountDownLatch(1);

    // Obtain the Request Token
    service.getRequestToken(new OAuthServiceAsync.AsyncTokenCallback()
    {
      public void onRequestToken(final Token requestToken)
      {
        System.out.println("Got the Request Token!");
        System.out.println();

        System.out.println("Now go and authorize Scribe here:");
        System.out.println(service.getAuthorizationUrl(requestToken));
        System.out.println("And paste the verifier here");
        System.out.print(">>");

        Scanner in = new Scanner(System.in);
        final Verifier verifier = new Verifier(in.nextLine());
        System.out.println();
        
        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        service.getAccessToken(requestToken, verifier, this);
      }

      public void onAccessToken(Token accessToken)
      {
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addBodyParameter("status", "this is sparta! *");
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
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