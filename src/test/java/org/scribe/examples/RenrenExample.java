package org.scribe.examples;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;

public class RenrenExample
{
  private static final String NETWORK_NAME = "Renren";
  private static final String PROTECTED_RESOURCE_URL = "http://api.renren.com/restserver.do";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "your api key";
    String apiSecret = "your api secret";
    OAuthService service = new ServiceBuilder()
        .provider(RenrenApi.class)
        .apiKey(apiKey)
        .apiSecret(apiSecret)
        .scope("status_update publish_feed")
        .callback("http://your.doman.com/oauth/renren")
        .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    // Obtain the Authorization URL
    System.out.println("Fetching the Authorization URL...");
    String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
    System.out.println("Got the Authorization URL!");
    System.out.println("Now go and authorize Scribe here:");
    System.out.println(authorizationUrl);
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
    OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("method", "users.getInfo");
    parameters.put("format", "json");
    parameters.put("v", "1.0");

    List<String> sigString = new ArrayList<String>(parameters.size() + 1);
    for (Map.Entry<String, String> entry : parameters.entrySet())
    {
      request.addQuerystringParameter(entry.getKey(), entry.getValue());
      sigString.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
    }
    sigString.add(String.format("%s=%s", OAuthConstants.ACCESS_TOKEN, accessToken.getToken()));
    Collections.sort(sigString);
    StringBuilder b = new StringBuilder();
    for (String param : sigString)
    {
      b.append(param);
    }
    b.append(apiSecret);
    System.out.println("Sig string: " + b.toString());
    request.addQuerystringParameter("sig", md5(b.toString()));
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

  }

  public static String md5(String orgString)
  {
    try
    {
      java.security.MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] array = md.digest(orgString.getBytes(Charset.forName("UTF-8")));
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i)
      {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
      }
      return sb.toString();
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    return null;
  }

}
