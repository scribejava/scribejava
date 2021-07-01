package com.github.scribejava.apis.jwt;

import java.io.Serializable;

public class JsonWebToken<H extends JsonWebTokenHeader, P extends JsonWebTokenPayload> implements Serializable {
  private static final long serialVersionUID = 5339251357087735195L;

  private final H header;
  private final P payload;
  private final String signature;

  public JsonWebToken(H header, P payload, String signature) {
    this.header = header;
    this.payload = payload;
    this.signature = signature;
  }

  public boolean isSigned() {
    return signature != null;
  }

  public H getHeader() {
    return header;
  }

  public P getPayload() {
    return payload;
  }

  public String getSignature() {
    return signature;
  }

  @Override
  public String toString() {
    return "JsonWebToken{" +
            "header=" + header +
            ", payload=" + payload +
            ", signature='" + signature + '\'' +
            '}';
  }
}
