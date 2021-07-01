package com.github.scribejava.apis.jwt;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonWebTokenPayload implements Serializable {
  private static final long serialVersionUID = 5854243661802871078L;

  @JsonProperty("iss")
  public String issuer;
  @JsonProperty("sub")
  public String subject;
  @JsonProperty("aud")
  public String audience;
  @JsonProperty("exp")
  public Long expires;
  @JsonProperty("nbf")
  public Long notBefore;
  @JsonProperty("jti")
  public String jwtId;
  @JsonProperty("iat")
  public Long issuedAt;
  @JsonAnySetter
  public final Map<String, Object> customFields = new HashMap<>();

  public Object get(String customField) {
    return customFields.get(customField);
  }

  @Override
  public String toString() {
    return "Payload{" +
        "issuer='" + issuer + '\'' +
        ", subject='" + subject + '\'' +
        ", audience='" + audience + '\'' +
        ", expires=" + expires +
        ", notBefore=" + notBefore +
        ", jwtId='" + jwtId + '\'' +
        ", issuedAt=" + issuedAt +
        ", customFields=" + customFields +
        '}';
  }
}
