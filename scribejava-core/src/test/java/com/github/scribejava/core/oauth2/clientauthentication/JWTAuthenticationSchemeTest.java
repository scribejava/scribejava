package com.github.scribejava.core.oauth2.clientauthentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Parameter;
import com.github.scribejava.core.model.Verb;

import org.junit.Test;

public class JWTAuthenticationSchemeTest {

  @Test
  public void shouldInjectSignedJWT() throws NoSuchAlgorithmException {
    final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(512);
    final KeyPair keyPair = keyGen.genKeyPair();
    final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    final String audience = "test";
    final String clientId = "test";
    final String keyId = "test";

    final JWTAuthenticationScheme scheme = JWTAuthenticationScheme.instance(privateKey, audience, keyId);

    final OAuthRequest request = new OAuthRequest(Verb.GET, "https://localhost");
    scheme.addClientAuthentication(request, clientId, clientId);

    assertTrue("Missing or wrong client assertion type",
                request.getBodyParams().contains(new Parameter(OAuthConstants.CLIENT_ASSERTION_TYPE,
                                                                OAuthConstants.CLIENT_ASSERTION_TYPE_JWT)));
    String jwt = null;
    for (Parameter param : request.getBodyParams().getParams()) {
      if (param.getKey().equals(OAuthConstants.CLIENT_ASSERTION)) {
        jwt = param.getValue();
      }
    }

    final JWTVerifier verified = JWT.require(Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), null))
            .build();
    final DecodedJWT decoded = verified.verify(jwt);
    assertTrue("Failed to validate audience", decoded.getAudience().contains(audience));
    assertEquals(clientId, decoded.getSubject());
    assertEquals(clientId, decoded.getIssuer());
    assertEquals(keyId, decoded.getKeyId());
  }

}
