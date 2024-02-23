package com.github.scribejava.apis.jwt;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonWebTokenHeader implements Serializable {
  private static final long serialVersionUID = 5224304413924414319L;

  @JsonProperty("alg")
  public String algorithm;
  @JsonProperty("typ")
  public String type;
  @JsonProperty("cty")
  public String contentType;
  @JsonAnySetter
  public final Map<String, Object> customFields = new HashMap<>();

  public Object get(String customField) {
    return customFields.get(customField);
  }

  @Override
  public String toString() {
    return "Header{" +
        "algorithm='" + algorithm + '\'' +
        ", type='" + type + '\'' +
        ", contentType='" + contentType + '\'' +
        ", customFields=" + customFields +
        '}';
  }
}
