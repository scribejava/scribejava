package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.builder.api.WithingsApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class WithingsExample {
    private static final String PROTECTED_RESOURCE_URL = "http://wbsapi.withings.net/measure?action=getmeas&userid=%d";

    private static final String apiKey = "";
    private static final String apiSecret = "";

    public static void main( String[] args ) {
        // If you choose to use a callback, "oauth_verifier" will be the return value by Twitter (request param)
        OAuthService service = new ServiceBuilder()
                .provider( WithingsApi.class )
                .apiKey( apiKey )
                .signatureType( SignatureType.QueryString )
                .apiSecret( apiSecret )
                .build();
        Scanner in = new Scanner( System.in );

        System.out.println( "=== Withing's OAuth Workflow ===" );
        System.out.println();

//        // Obtain the Request Token
        System.out.println( "Fetching the Request Token..." );
        Token requestToken = service.getRequestToken();
        System.out.println( "Got the Request Token!" );
        System.out.println();

        System.out.println( "Now go and authorize Scribe here:" );
        System.out.println( service.getAuthorizationUrl( requestToken ) );
        System.out.println( "paste the userid here ");
        System.out.print( ">>" );
        Long id = Long.parseLong(in.nextLine());

        System.out.println( "And paste the verifier here" );
        System.out.print( ">>" );
        Verifier verifier = new Verifier( in.nextLine() );
        System.out.println();

//        // Trade the Request Token and Verfier for the Access Token
        System.out.println( "Trading the Request Token for an Access Token..." );
        Token accessToken = service.getAccessToken( requestToken, verifier );
        System.out.println( "Got the Access Token!" );
        System.out.println( "(if your curious it looks like this: " + accessToken + " )" );
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println( "Now we're going to access a protected resource..." );

        OAuthRequest request = new OAuthRequest( Verb.GET, String.format(PROTECTED_RESOURCE_URL, id ) );
        service.signRequest( accessToken, request );
        Response response = request.send();
        System.out.println( "Got it! Lets see what we found..." );
        System.out.println();
        System.out.println( response.getBody() );

        System.out.println();
        System.out.println( "Thats it man! Go and build something awesome with Scribe! :)" );
    }

}
