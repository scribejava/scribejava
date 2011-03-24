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
 * Date: 23.03.2011
 * Time: 12:02:53
 */
public class MailruOAuth20ServiceImpl extends OAuth20ServiceImpl {
    /**
     * Default constructor
     *
     * @param api    OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public MailruOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
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

    private String calculateSig(Map<String, String> parametersMap, String apiSecret) {
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
        // Appending secret key
        sb.append(apiSecret);
        //
        return HexStringsConverter.toHexString(messageDigest.digest(sb.toString().getBytes()));
    }

    /**
     * Sign request for Mail.ru API.
     * http://api.mail.ru/docs/guides/restapi/
     * Adds app_id, session_key (accessToken) and sig (md5 hash of parameters and secret api key) to request.
     * @param accessToken
     * @param request
     */
    @Override
    public void signRequest(Token accessToken, OAuthRequest request) {
        Map<String, String> parametersMap = null;
        boolean forceBodyParameters = false;
        if (request.getVerb() == Verb.GET) {
            request.addQuerystringParameter("session_key", accessToken.getToken());
            request.addQuerystringParameter("app_id", config.getApiKey());
            request.addQuerystringParameter("secure", "1");
            parametersMap = request.getQueryStringParams();
        } else if (request.getVerb() == Verb.POST) {
            if (request.getBodyParams().size() > 0) {
                forceBodyParameters = true;
                if (request.getQueryStringParams().size() > 0) {
                    throw new UnsupportedOperationException("Ambigiuos parameters passing - using both QueryStringParameters and BodyParameters.");
                }
            }
            if (forceBodyParameters) {
                request.addBodyParameter("app_id", config.getApiKey());
                request.addBodyParameter("session_key", accessToken.getToken());
                request.addBodyParameter("secure", "1");
                parametersMap = request.getBodyParams();
            } else {
                request.addQuerystringParameter("app_id", config.getApiKey());
                request.addQuerystringParameter("session_key", accessToken.getToken());
                request.addQuerystringParameter("secure", "1");
                parametersMap = request.getQueryStringParams();
            }
        } else {
            throw new UnsupportedOperationException("Unknown verb.");
        }
        //
        String sig = calculateSig(parametersMap, config.getApiSecret());
        if (forceBodyParameters) {
            if (request.getVerb() != Verb.POST) {
                throw new IllegalStateException("Assertion failed.");
            }
            request.addBodyParameter("sig", sig);
        } else {
            request.addQuerystringParameter("sig", sig);
        }
    }
}
