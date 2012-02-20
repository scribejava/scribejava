package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.BistriApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class BistriExample
{
    private static final String NETWORK_NAME = "Bistri";

    private static final String AUTHORIZE_URL = "http://localhost:8080/oauth/authorize?oauth_token=";

    private static final String PROTECTED_RESOURCE_URL = "http://localhost:8080/static/supportedlanguages";

    public static void main( String[] args )
    {
        OAuthService service =
            new ServiceBuilder().provider( BistriApi.class ).apiKey( "testUser" ).apiSecret( "testSecret" ).build();
        Scanner in = new Scanner( System.in );

        System.out.println( "=== " + NETWORK_NAME + "'s OAuth Workflow ===" );
        System.out.println();

        // Obtain the Request Token
        System.out.println( "Fetching the Request Token..." );
        Token requestToken = service.getRequestToken();
        System.out.println( "Got the Request Token!" );
        System.out.println( "(if your curious it looks like this: " + requestToken + " )" );
        System.out.println();

        System.out.println( "Now go and authorize Scribe here:" );
        System.out.println( AUTHORIZE_URL + requestToken.getToken() );
        System.out.println( "And paste the verifier here" );
        System.out.print( ">>" );
        Verifier verifier = new Verifier( in.nextLine() );
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println( "Trading the Request Token for an Access Token..." );
        Token accessToken = service.getAccessToken( requestToken, verifier );
        System.out.println( "Got the Access Token!" );
        System.out.println( "(if your curious it looks like this: " + accessToken + " )" );
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

    }
}
