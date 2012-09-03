package org.scribe.examples;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

/**
 * @author Alexander M. Petrovsky <askjuise@gmail.com>
 * @since 07.06.2012
 */
public class EchoExample
{
  private static final String PROTECTED_RESOURCE_URL = "http://api.echoenabled.com/v1/submit";

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    final String key = "your key";
    final String secret = "your secret";

    // In 2-legged model, oauth_token must be empty
    Token token = new Token("", "");
    OAuthService service = new ServiceBuilder().apiKey(key).apiSecret(secret).provider(EchoApi.class).build();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
    request.addBodyParameter("content", "your valid activity streams xml");
    service.signRequest(token, request);
    Response response = request.send();
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }
}
