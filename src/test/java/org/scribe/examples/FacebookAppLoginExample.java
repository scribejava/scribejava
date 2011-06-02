package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class FacebookAppLoginExample 
{
  private static final String NETWORK_NAME = "Facebook";
  private static final Token EMPTY_TOKEN = null;
  private static final String PROTECTED_RESOURCE_URL ="https://graph.facebook.com/%s/insights";

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    
    String apiKey = "your_app_id";
    String apiSecret = "your_api_secret";
    String callbackURL = "your_call_back";
    OAuthService service = new ServiceBuilder()
                                  .provider(FacebookApi.class)
                                  .apiKey(apiKey)
                                  .apiSecret(apiSecret)
                                  .callback(callbackURL)
                                  .grantType(OAuthConstants.CLIENT_CREDENTIALS)
                                  .build();

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();
    System.out.println("Getting an access Token with Client Credentials (a.k.a App Login as Facebook defines)");
    Token accessToken = service.getAccessToken(EMPTY_TOKEN, null);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL,apiKey));
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
