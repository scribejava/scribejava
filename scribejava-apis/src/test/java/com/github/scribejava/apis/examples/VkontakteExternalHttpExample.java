package com.github.scribejava.apis.examples;

import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.VkontakteApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.httpclient.ahc.AhcHttpClient;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

public class VkontakteExternalHttpExample {

    private static final String NETWORK_NAME = "vk.com";
    private static final String PROTECTED_RESOURCE_URL = "https://api.vk.com/method/users.get?v="
            + VkontakteApi.VERSION;

    private VkontakteExternalHttpExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";

        //create any http client externally
        final DefaultAsyncHttpClientConfig httpClientConfig = new DefaultAsyncHttpClientConfig.Builder()
                .setMaxConnections(5)
                .setRequestTimeout(10_000)
                .setPooledConnectionIdleTimeout(1_000)
                .setReadTimeout(1_000)
                .build();
        //wrap it
        try ( DefaultAsyncHttpClient ahcHttpClient = new DefaultAsyncHttpClient(httpClientConfig)) {
            //wrap it
            final AhcHttpClient wrappedAHCHttpClient = new AhcHttpClient(ahcHttpClient);

            final OAuth20Service service = new ServiceBuilder(clientId)
                    .httpClient(wrappedAHCHttpClient)
                    .apiSecret(clientSecret)
                    .defaultScope("wall,offline") // replace with desired scope
                    .callback("http://your.site.com/callback")
                    .build(VkontakteApi.instance());
            final Scanner in = new Scanner(System.in);

            System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
            System.out.println();

            // Obtain the Authorization URL
            System.out.println("Fetching the Authorization URL...");
            final String authorizationUrl = service.getAuthorizationUrl();
            System.out.println("Got the Authorization URL!");
            System.out.println("Now go and authorize ScribeJava here:");
            System.out.println(authorizationUrl);
            System.out.println("And paste the authorization code here");
            System.out.print(">>");
            final String code = in.nextLine();
            System.out.println();

            System.out.println("Trading the Authorization Code for an Access Token...");
            final OAuth2AccessToken accessToken = service.getAccessToken(code);
            System.out.println("Got the Access Token!");
            System.out.println("(The raw response looks like this: " + accessToken.getRawResponse()
                    + "')");
            System.out.println();

            // Now let's go and ask for a protected resource!
            System.out.println("Now we're going to access a protected resource...");
            final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
            service.signRequest(accessToken, request);
            try ( Response response = service.execute(request)) {
                System.out.println("Got it! Lets see what we found...");
                System.out.println();
                System.out.println(response.getCode());
                System.out.println(response.getBody());
            }
            System.out.println();
            System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
        }
    }
}
