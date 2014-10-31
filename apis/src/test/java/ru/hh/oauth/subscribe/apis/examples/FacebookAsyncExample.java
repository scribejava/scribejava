package ru.hh.oauth.subscribe.apis.examples;

import com.ning.http.client.AsyncHttpClientConfig;
import java.util.Random;
import java.util.Scanner;
import ru.hh.oauth.subscribe.core.builder.ServiceBuilderAsync;
import ru.hh.oauth.subscribe.apis.FacebookApi;
import ru.hh.oauth.subscribe.core.model.ForceTypeOfHttpRequest;
import ru.hh.oauth.subscribe.core.model.SubScribeConfig;
import ru.hh.oauth.subscribe.core.model.OAuthRequestAsync;
import ru.hh.oauth.subscribe.core.model.Response;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.model.Verifier;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;

public class FacebookAsyncExample {

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/v2.0/me";
    private static final Token EMPTY_TOKEN = null;

    public static void main(String[] args) throws Exception {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final String secretState = "secret" + new Random().nextInt(999_999);
        SubScribeConfig.setForceTypeOfHttpRequests(ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS);
        final AsyncHttpClientConfig clientConfig = new AsyncHttpClientConfig.Builder()
                .setMaximumConnectionsTotal(5)
                .setRequestTimeoutInMs(10000)
                .setAllowPoolingConnection(false)
                .setIdleConnectionInPoolTimeoutInMs(1000)
                .setIdleConnectionTimeoutInMs(1000)
                .build();

        final OAuthService service = new ServiceBuilderAsync()
                .provider(FacebookApi.class)
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback("http://www.example.com/oauth_callback/")
                .asyncHttpClientConfig(clientConfig)
                .build();

        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s Async OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize SubScribe here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        System.out.println("And paste the state from server here. We have set 'secretState'='" + secretState + "'.");
        System.out.print(">>");
        final String value = in.nextLine();
        if (secretState.equals(value)) {
            System.out.println("State value does match!");
        } else {
            System.out.println("Ooops, state value does not match!");
            System.out.println("Expected = " + secretState);
            System.out.println("Got      = " + value);
            System.out.println();
        }

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final Token accessToken = service.getAccessTokenAsync(EMPTY_TOKEN, verifier, null).get();
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequestAsync request = new OAuthRequestAsync(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        final Response response = request.sendAsync(null).get();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with SubScribe! :)");
        service.closeAsyncClient();
    }
}
