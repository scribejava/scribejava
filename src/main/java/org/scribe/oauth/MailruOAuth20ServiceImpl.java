package org.scribe.oauth;

import java.util.SortedMap;
import java.util.TreeMap;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;

import java.util.Map;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Mail.ru {@link OAuth20ServiceImpl) customization
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

    // sig = md5( request_params_composed_string + application_secret_key )
    private String calculateSig(Map<String, String> parametersMap, String apiSecret) {
        // Sort parameters alphabetically before performing a sign
        SortedMap<String, String> sorted = new TreeMap<String, String>(parametersMap);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        // Appending secret key
        sb.append(apiSecret);
        //
        return md5Hex(sb.toString());
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
