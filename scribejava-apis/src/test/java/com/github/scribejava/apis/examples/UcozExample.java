package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.UcozApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class UcozExample {
    public static void main(String ...args){
        String PROTECTED_RESOURCE_URL ="http://artmurka.com/uapi/shop/request?page=categories";
        OAuth10aService service = new ServiceBuilder( "your_api_key")
                .apiSecret("your_api_secret")
                .debug()
                .build(UcozApi.instance());
        Scanner in = new Scanner(System.in);
        OAuth1RequestToken requestToken = null;
        try {
            requestToken = service.getRequestToken();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Got the Request Token!");
        System.out.println();

        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(service.getAuthorizationUrl(requestToken));
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        final String oauthVerifier = in.nextLine();
        System.out.println();
        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        OAuth1AccessToken accessToken = null;
        try {
            accessToken = service.getAccessToken(requestToken, oauthVerifier);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        Response response = null;
        try {
            response = service.execute(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        try {
            System.out.println(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
