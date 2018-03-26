package com.github.scribejava.core.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.github.scribejava.core.exceptions.OAuthSignatureException;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * HMAC-SHA1 implementation of {@link SignatureService}
 * https://tools.ietf.org/html/rfc5849#section-3.4.2
 */
public class HMACSha1SignatureService implements SignatureService {

    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String METHOD = "HMAC-SHA1";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            Preconditions.checkEmptyString(baseString, "Base string cant be null or empty string");
            Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | RuntimeException e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, InvalidKeyException {
        final SecretKeySpec key = new SecretKeySpec(keyString.getBytes(UTF8), HMAC_SHA1);
        final Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        final byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return BASE_64_ENCODER.encodeToString(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignatureMethod() {
        return METHOD;
    }
}
