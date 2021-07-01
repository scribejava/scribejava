package com.github.scribejava.apis.esia;

public interface SigningService {
  String createSignature(String stringToSign);
}
