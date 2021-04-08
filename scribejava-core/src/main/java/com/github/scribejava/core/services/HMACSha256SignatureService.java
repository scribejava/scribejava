package com.github.scribejava.core.services;

import com.github.scribejava.core.base64.Base64;
import com.github.scribejava.core.exceptions.OAuthSignatureException;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * HMAC-SHA256 implementation of {@link SignatureService}
 */
public class HMACSha256SignatureService implements SignatureService {

    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String METHOD = "HMAC-SHA256";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            Preconditions.checkEmptyString(baseString, "Base string can't be null or empty string");
            Preconditions.checkNotNull(apiSecret, "Api secret can't be null");
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        } catch (NoSuchAlgorithmException | InvalidKeyException | RuntimeException e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) throws NoSuchAlgorithmException, InvalidKeyException {
        final SecretKeySpec key = new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        final Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(key);
        final byte[] bytes = mac.doFinal(toSign.getBytes(StandardCharsets.UTF_8));
        return Base64.encode(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignatureMethod() {
        return METHOD;
    }
}
