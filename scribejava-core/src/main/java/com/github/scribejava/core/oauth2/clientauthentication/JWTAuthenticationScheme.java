package com.github.scribejava.core.oauth2.clientauthentication;

import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;

/**
 * 2.2. Using JWTs for Client Authentication<br>
 * 3. JWT Format and Processing Requirements<br>
 * https://tools.ietf.org/html/rfc7523#section-2.2 <br>
 * JWTs for Client Authentication scheme
 */
public class JWTAuthenticationScheme implements ClientAuthentication {
  private static final ConcurrentMap<String, JWTAuthenticationScheme> INSTANCES = new ConcurrentHashMap<>();
  private final RSAPrivateKey privateKey;
  private final String audience;
  private final String keyId;

  public JWTAuthenticationScheme(RSAPrivateKey privateKey, String audience, String keyId) {
    this.privateKey = privateKey;
    this.audience = audience;
    this.keyId = keyId;
  }

  public static JWTAuthenticationScheme instance(RSAPrivateKey privateKey, String audience, String keyId) {
    final String instanceId = audience + keyId;

    JWTAuthenticationScheme scheme = INSTANCES.get(instanceId);
    if (scheme == null) {
      scheme = new JWTAuthenticationScheme(privateKey, audience, keyId);
      final JWTAuthenticationScheme alreadyCreatedScheme = INSTANCES.putIfAbsent(instanceId, scheme);
      if (alreadyCreatedScheme != null) {
        return alreadyCreatedScheme;
      }
    }
    return scheme;
  }

  @Override
  public void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret) {
    final String token = this.createJwtSignedHMAC(apiKey);
    request.getBodyParams().add(OAuthConstants.CLIENT_ASSERTION_TYPE, OAuthConstants.CLIENT_ASSERTION_TYPE_JWT);
    request.getBodyParams().add(OAuthConstants.CLIENT_ASSERTION, token);
  }

  public String createJwtSignedHMAC(String apiKey) {
    final Algorithm algorithm = Algorithm.RSA256(null, privateKey);
    final Calendar c = Calendar.getInstance();
    final Date now = c.getTime();
    c.add(Calendar.SECOND, 60);
    final Date exp = c.getTime();

    return JWT.create()
    .withKeyId(keyId)
    .withIssuer(apiKey)
    .withSubject(apiKey)
    .withJWTId(UUID.randomUUID().toString())
    .withAudience(this.audience)
    .withIssuedAt(now)
    .withNotBefore(now)
    .withExpiresAt(exp)
    .sign(algorithm);
  }
}
