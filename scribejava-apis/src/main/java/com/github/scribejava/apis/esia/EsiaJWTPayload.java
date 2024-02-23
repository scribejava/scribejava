package com.github.scribejava.apis.esia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.scribejava.apis.jwt.JsonWebTokenPayload;

public class EsiaJWTPayload extends JsonWebTokenPayload {
  @JsonProperty("auth_time")
  public Long authorizationTime;
  @JsonProperty("urn:esia:amd")
  public String authorizationMethod;
  @JsonProperty("amr")
  public String authenticationMethod;
  @JsonProperty("urn:esia:sid")
  public String sessionId;
  @JsonProperty("urn:esia:sbj")
  public EsiaJWTSubject esiaSubject;

  @Override
  public String toString() {
    return "EsiaJWTPayload{" +
        "authorizationTime=" + authorizationTime +
        ", authorizationMethod='" + authorizationMethod + '\'' +
        ", authenticationMethod='" + authenticationMethod + '\'' +
        ", sessionId='" + sessionId + '\'' +
        ", esiaSubject=" + esiaSubject +
        ", basicPayload=" + super.toString() +
        '}';
  }
}
