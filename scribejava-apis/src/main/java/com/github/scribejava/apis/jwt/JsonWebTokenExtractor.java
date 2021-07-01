package com.github.scribejava.apis.jwt;

import com.github.scribejava.core.base64.Base64;
import com.github.scribejava.core.extractors.AbstractJsonExtractor;
import com.github.scribejava.core.utils.Preconditions;
import java.io.IOException;

public class JsonWebTokenExtractor<H extends JsonWebTokenHeader, P extends JsonWebTokenPayload>
        extends AbstractJsonExtractor {
  private final Class<H> headerClass;
  private final Class<P> payloadClass;

  public JsonWebTokenExtractor(Class<H> headerClass, Class<P> payloadClass) {
    this.headerClass = headerClass;
    this.payloadClass = payloadClass;
  }

  public static JsonWebTokenExtractor<JsonWebTokenHeader, JsonWebTokenPayload> basic() {
    return new JsonWebTokenExtractor<>(JsonWebTokenHeader.class, JsonWebTokenPayload.class);
  }

  public static <H extends JsonWebTokenHeader, P extends JsonWebTokenPayload> JsonWebTokenExtractor<H, P> custom(
          Class<H> headerClass, Class<P> payloadClass) {
    return new JsonWebTokenExtractor<>(headerClass, payloadClass);
  }

  public JsonWebToken<H, P> extract(String rawJsonWebToken) throws IOException {
    final String[] parts = rawJsonWebToken.split("\\.");
    Preconditions.check(parts.length >= 2, "Raw string has less than 2 dot-separated parts");
    Preconditions.check(parts.length <= 3, "Raw string has more than 3 dot-separated parts");

    final H header = decodePart(parts[0], headerClass);
    final P payload = decodePart(parts[1], payloadClass);
    return new JsonWebToken<>(header, payload, parts.length == 3 ? parts[2] : null);
  }

  protected static <T> T decodePart(String part, Class<T> valueClass) throws IOException {
    final byte[] bytes = Base64.decodeUrl(part);
    return OBJECT_MAPPER.readValue(bytes, valueClass);
  }
}
