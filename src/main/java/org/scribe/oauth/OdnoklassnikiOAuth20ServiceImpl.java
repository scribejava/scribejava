package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;
import org.scribe.utils.HexStringsConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * User: elwood
 * Date: 24.03.2011
 * Time: 16:40:30
 */
public class OdnoklassnikiOAuth20ServiceImpl extends OAuth20ServiceImpl {

    /**
     * Default constructor
     *
     * @param api    OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OdnoklassnikiOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        //
        if (null == api) {
            throw new IllegalArgumentException("api");
        }
        if (null == config) {
            throw new IllegalArgumentException("config");
        }
        //
        this.api = api;
        this.config = config;
    }

    private DefaultApi20 api;
    private OAuthConfig config;
    private String publicKey;

    /**
     * Additional public api key, need for API invocation (used to calculate signature in requestSign method).
     * @return
     */
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        if (null == publicKey || publicKey.isEmpty()) {
            throw new IllegalArgumentException("publicKey");
        }
        //
        this.publicKey = publicKey;
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter("grant_type", "authorization_code");
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        //
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    // sig = md5( request_params_composed_string+ md5(access_token + application_secret_key)  )
    private String calculateSig(Map<String, String> parametersMap, String accessTokenValue, String apiSecret) {
        String accessTokenPlusSecretMd5 = HexStringsConverter.toHexString(messageDigest.digest((accessTokenValue + apiSecret).getBytes()));
        //
        Object[] keysArray = parametersMap.keySet().toArray();
        // Sort parameters alphabetically before performing a sign
        Arrays.sort(keysArray);
        int length = keysArray.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            String key = (String) keysArray[i];
            sb.append(key);
            sb.append("=");
            sb.append(parametersMap.get(key));
        }
        //
        String wholeStringMd5 = HexStringsConverter.toHexString(messageDigest.digest((sb.toString() + accessTokenPlusSecretMd5).getBytes()));
        return wholeStringMd5;
    }

    @Override
    public void signRequest(Token accessToken, OAuthRequest request) {
        if (null == publicKey || publicKey.isEmpty()) {
            throw new IllegalStateException("publicKey must be initialized before signing requests.");
        }
        //
        if (request.getVerb() != Verb.POST) {
            throw new UnsupportedOperationException("Only POST verb is supported now in odnoklassniki.ru API calls.");
        }
        request.addQuerystringParameter("application_key", this.publicKey);
        //
        String sig = calculateSig(request.getQueryStringParams(), accessToken.getToken(), this.config.getApiSecret());
        request.addQuerystringParameter("sig", sig);
        request.addQuerystringParameter("access_token", accessToken.getToken());
    }
}
