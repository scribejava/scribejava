package org.scribe.examples;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import java.util.*;

/**
 * Uses an app-specific token for performing a public search for videos on vimeo
 * You can run it from the main folder by:
 * 1. Replacing apiKey, apiSecret and accessToken with the values specific to your app
 * 2. Running the following at the project root:
 *    mvn compiler:compile
 *    mvn exec:java -Dexec.mainClass="org.scribe.examples.Vimeo20Example" -Dexec.classpathScope=test test-compile exec:java
 */
public class Vimeo20Example
{
  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "replace-with-your-own";
    String apiSecret = "replace-with-your-own";
    String accessToken = "replace-with-your-own";

    OAuthService vimeoService = new ServiceBuilder()
                 .provider(VimeoApi20.class)
                 .apiKey( apiKey )
                 .apiSecret( apiSecret )
                 .build();



    OAuthRequest myrequest = new OAuthRequest(Verb.GET,"https://api.vimeo.com/videos?per_page=3&query=good%20video");

    Token mytoken = new Token(accessToken, apiSecret);
    vimeoService.signRequest(mytoken, myrequest); 
    Response response = myrequest.send();

    System.out.println("Fetched the following from vimeo:");
    System.out.println(response.getBody());
  }
}
