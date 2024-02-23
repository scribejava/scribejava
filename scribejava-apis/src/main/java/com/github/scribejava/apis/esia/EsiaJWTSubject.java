package com.github.scribejava.apis.esia;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EsiaJWTSubject {
  @JsonProperty("urn:esia:sbj:typ")
  public String type;
  @JsonProperty("urn:esia:sbj:is_tru")
  public Boolean trusted;
  @JsonProperty("urn:esia:sbj:oid")
  public Long oid;
  @JsonProperty("urn:esia:sbj:nam")
  public String name;

  @Override
  public String toString() {
    return "EsiaJWTSubject{" +
        "type='" + type + '\'' +
        ", trusted=" + trusted +
        ", oid=" + oid +
        ", name='" + name + '\'' +
        '}';
  }
}
