package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.BoxApi20;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class Box2Example {
    private static final String NETWORK_NAME = "Box 2.0";

    private static final String PROTECTED_RESOURCE_URL = "https://api.box.com/2.0/users";


    public static void main( String[] args )
    {
        // Replace these with your own api key, secret and callbagck
        String apiKey = "apikey";
        String apiSecret = "apisecret";
        String callback = "http://localhost:8080";

        OAuthService service =
            new ServiceBuilder().provider( BoxApi20.class ).apiKey( apiKey ).apiSecret( apiSecret ).callback(
                callback ).grantType( OAuthConstants.AUTHORIZATION_CODE ).build();

        System.out.println( "=== " + NETWORK_NAME + "'s OAuth Workflow ===" );
        System.out.println();

        // Obtain the authorization url
        String url = service.getAuthorizationUrl( null );

        System.out.println( "go there : \n" + url );
        System.out.println( "paste the authorization code >>" );
        Scanner in = new Scanner( System.in );
        Verifier verifier = new Verifier( in.nextLine() );

        Token accessToken = service.getAccessToken( null, verifier );

        System.out.println( "Got the access Token!" );
        System.out.println( "(if your curious it looks like this: " + accessToken + " )" );
        System.out.println();
        
        Token refreshToken = service.getRefreshToken();
        System.out.println( "Refresh token: " + refreshToken );
        System.out.println();

        
        // Now let's go and ask for a protected resource!
        System.out.println( "Now we're going to access a protected resource..." );
        OAuthRequest request = new OAuthRequest( Verb.GET, PROTECTED_RESOURCE_URL );
        service.signRequest( accessToken, request );
        Response response = request.send();
        System.out.println( "Got it! Lets see what we found..." );
        System.out.println();
        System.out.println( response.getCode() );
        System.out.println( response.getBody() );

        System.out.println();
        System.out.println( "Thats it man! Go and build something awesome with Scribe! :)" );
        
        System.out.println();
        System.out.println( "Trying to refresh a new access token" );
        Token newAccessToken = service.refreshAccessToken(refreshToken);
        System.out.println( "New access token: " + newAccessToken );
        System.out.println( "New refresh token: " + service.getRefreshToken());
        System.out.println();
    }
}