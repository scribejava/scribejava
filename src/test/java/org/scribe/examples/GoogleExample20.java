package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi20;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class GoogleExample20
{
    private static final String NETWORK_NAME = "Google 2.0";

    private static final String PROTECTED_RESOURCE_URL = "https://docs.google.com/feeds/default/private/full/";

    private static final String SCOPE = "https://docs.google.com/feeds/";

    public static void main( String[] args )
    {
        // Replace these with your own api key, secret and callback
        String apiKey = "apiKey";
        String apiSecret = "apiSecret";
        String callback = "http://www.example.com/google/back";

        OAuthService service =
            new ServiceBuilder().provider( GoogleApi20.class ).apiKey( apiKey ).apiSecret( apiSecret ).callback(
                callback ).scope( SCOPE ).grantType( OAuthConstants.AUTHORIZATION_CODE ).build();

        System.out.println( "=== " + NETWORK_NAME + "'s OAuth Workflow ===" );
        System.out.println();

        // Obtain the authorization url
        String url = service.getAuthorizationUrl( null );

        System.out.println( "go there : " + url );
        System.out.println( "paste the authorization code >>" );
        Scanner in = new Scanner( System.in );
        Verifier verifier = new Verifier( in.nextLine() );

        Token accessToken = service.getAccessToken( null, verifier );

        System.out.println( "Got the access Token!" );
        System.out.println( "(if your curious it looks like this: " + accessToken + " )" );
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println( "Now we're going to access a protected resource..." );
        OAuthRequest request = new OAuthRequest( Verb.GET, PROTECTED_RESOURCE_URL );
        service.signRequest( accessToken, request );
        request.addHeader( "GData-Version", "3.0" );
        Response response = request.send();
        System.out.println( "Got it! Lets see what we found..." );
        System.out.println();
        System.out.println( response.getCode() );
        System.out.println( response.getBody() );

        System.out.println();
        System.out.println( "Thats it man! Go and build something awesome with Scribe! :)" );

    }
}