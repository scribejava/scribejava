package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Layer7Api20;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class Layer7test2
{

  private static String resourceUrl = "https://cdryden-pc.l7tech.local:9443/oauth/v2/protectedapi";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "54f0c455-4d80-421f-82ca-9194df24859d";
    String apiSecret = "a0f2742f-31c7-436f-9802-b7015b8fd8e6";
    OAuthService service = new ServiceBuilder()
                                  .provider(Layer7Api20.class)
                                  .apiKey(apiKey)
                                  .apiSecret(apiSecret)
                                  .callback("http://cdryden-pc.l7tech.local")
                                  .scope("oob")
                                  .build();
    
//    if (((String)args[1]).equals("getToken")){
//    Verifier verifier = new Verifier(args[2]);
//    Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
//    System.out.println(accessToken.getToken());
//    System.out.println(accessToken.getSecret());
//    }
    //else
    {
      Token accessToken = new Token("2cb3a91c-6019-4c77-9701-dfeaf960e343","");

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, resourceUrl);
    service.signRequest(accessToken, request);
    System.out.println(request.getCompleteUrl());
    System.out.println(request.getHeaders());
    Response response = request.send();
    
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
    }
}}
