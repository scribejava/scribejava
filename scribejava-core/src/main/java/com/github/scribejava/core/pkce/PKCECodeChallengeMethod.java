package com.github.scribejava.core.pkce;

import com.github.scribejava.core.base64.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum PKCECodeChallengeMethod {
    S256 {
        @Override
        public String transform2CodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
            return Base64.encodeUrlWithoutPadding(
                    MessageDigest.getInstance("SHA-256").digest(codeVerifier.getBytes(StandardCharsets.US_ASCII)));
        }
    },
    PLAIN {
        @Override
        public String transform2CodeChallenge(String codeVerifier) {
            return codeVerifier;
        }
    };

    public abstract String transform2CodeChallenge(String codeVerifier) throws NoSuchAlgorithmException;
}
