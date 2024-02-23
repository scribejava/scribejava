package com.github.scribejava.apis.esia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.scribejava.apis.jwt.JsonWebTokenHeader;

public class EsiaJWTHeader extends JsonWebTokenHeader {
  @JsonProperty("ver")
  public Integer version;
  @JsonProperty("sbt")
  public String tokenType;

  @Override
  public String toString() {
    return "EsiaJWTHeader{" +
        "version=" + version +
        ", tokenType='" + tokenType + '\'' +
        ", basicHeader=" + super.toString() +
        '}';
  }
}
