package org.scribe.examples;

import java.util.Scanner;
import java.util.UUID;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedIn2Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @company: 搜狐

 * @author: zhenpengzhang
 
 * @createtime: 2014年8月26日 下午4:44:28

 * @version:
 
 */
public class LinkedIn2Example {

    public static final String PROTECTED_RESOURCE_URL = "https://api.linkedin.com/v1/people/~";
    public static final String API_KEY                = "75behd12ztnmus";
    public static final String SECRET                 = "EBono1sp6nfbshXw";
    public static final Token  EMPTY_TOKEN            = null;
    public static final String CALLBACK_URL           = "http://fatfacesheep.sinaapp.com";

    public static void main(String[] args) {
        // Replace these with your own api key and secret
        OAuthService service = new ServiceBuilder().provider(LinkedIn2Api.class).apiKey(API_KEY)
            .apiSecret(SECRET).callback(CALLBACK_URL).build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== LinkedIn2's OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize Scribe here:");
        System.out.println(authorizationUrl + "&state=" + UUID.randomUUID().toString());
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
