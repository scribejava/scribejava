package org.scribe.oauth;

import java.util.Map;

import org.scribe.builder.api.NkApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

public class NKOAuthServiceImpl implements OAuthService {

    private static final String VERSION = "1.0";
    private static String GRANT_TYPE = "grant_type";
    private static String AUTHORIZATION_CODE = "authorization_code";

    private final OAuthConfig config;
    private final NkApi api;

    public NKOAuthServiceImpl(NkApi api, OAuthConfig config) {
        this.config = config;
        this.api = api;
    }

    public void signRequest(Token token, OAuthRequest request) {
        request.getOauthParameters().put("nk_token", token.getToken());
        addOAuthParams(request, token);
        addSignature(request);
    }

    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        request.addBodyParameter(GRANT_TYPE, AUTHORIZATION_CODE);
        if (config.hasScope()) {
            request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
        }
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    public String getAuthorizationUrl(Token requestToken) {
        return api.getAuthorizationUrl(config);
    }

    public Token getRequestToken() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public String getVersion() {
        return VERSION;
    }

    private void addOAuthParams(OAuthRequest request, Token token) {
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
        if (config.hasScope())
            request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
        request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
    }

    private void addSignature(OAuthRequest request) {
        switch (config.getSignatureType()) {
        case Header:
            String oauthHeader = api.getHeaderExtractor().extract(request);
            request.addHeader(OAuthConstants.HEADER, oauthHeader);
            break;
        case QueryString:
            for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet()) {
                request.addQuerystringParameter(entry.getKey(), entry.getValue());
            }
            break;
        }
    }

    private String getSignature(OAuthRequest request, Token token) {
        String baseString = api.getBaseStringExtractor().extract(request);
        return api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());
    }

}
