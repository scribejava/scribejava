package com.github.scribejava.core.extractors;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth2.OAuth2Error;
import com.github.scribejava.core.utils.Preconditions;
import java.util.Map;

/**
 * JSON (default) implementation of {@link TokenExtractor} for OAuth 2.0
 */
public class OAuth2AccessTokenJsonExtractor implements TokenExtractor<OAuth2AccessToken> {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected OAuth2AccessTokenJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final OAuth2AccessTokenJsonExtractor INSTANCE = new OAuth2AccessTokenJsonExtractor();
    }

    public static OAuth2AccessTokenJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public OAuth2AccessToken extract(Response response) throws IOException {
        final String body = response.getBody();
        Preconditions.checkEmptyString(body, "Response body is incorrect. Can't extract a token from an empty string");

        if (response.getCode() != 200) {
            generateError(body);
        }
        return createToken(body);
    }

    /**
     * Related documentation: https://tools.ietf.org/html/rfc6749#section-5.2
     *
     * @param rawResponse response
     * @throws IOException IOException
     */
    public void generateError(String rawResponse) throws IOException {
        @SuppressWarnings("unchecked")
        final Map<String, String> response = OBJECT_MAPPER.readValue(rawResponse, Map.class);

        final String errorUriInString = response.get("error_uri");
        URI errorUri;
        try {
            errorUri = errorUriInString == null ? null : URI.create(errorUriInString);
        } catch (IllegalArgumentException iae) {
            errorUri = null;
        }

        OAuth2Error errorCode;
        try {
            errorCode = OAuth2Error.parseFrom(extractRequiredParameter(response, "error", rawResponse));
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        throw new OAuth2AccessTokenErrorResponse(errorCode, response.get("error_description"), errorUri, rawResponse);
    }

    private OAuth2AccessToken createToken(String rawResponse) throws IOException {

        @SuppressWarnings("unchecked")
        final Map<String, String> response = OBJECT_MAPPER.readValue(rawResponse, Map.class);

        final String expiresInString = response.get("expires_in");
        Integer expiresIn;
        try {
            expiresIn = expiresInString == null ? null : Integer.valueOf(expiresInString);
        } catch (NumberFormatException nfe) {
            expiresIn = null;
        }

        return createToken(extractRequiredParameter(response, OAuthConstants.ACCESS_TOKEN, rawResponse),
                response.get("token_type"), expiresIn, response.get(OAuthConstants.REFRESH_TOKEN),
                response.get(OAuthConstants.SCOPE), response, rawResponse);
    }

    protected OAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, Map<String, String> response, String rawResponse) {
        return createToken(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
    }

    /**
     *
     * @param accessToken accessToken
     * @param tokenType tokenType
     * @param expiresIn expiresIn
     * @param refreshToken refreshToken
     * @param scope scope
     * @param rawResponse rawResponse
     * @return OAuth2AccessToken
     * @deprecated use {@link #createToken(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String,
     * java.lang.String, java.util.Map, java.lang.String)}
     */
    @Deprecated
    protected OAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, String rawResponse) {
        return new OAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
    }

    /**
     *
     * @param response response
     * @param regexPattern regexPattern
     * @param required required
     * @return parameter value
     * @throws OAuthException OAuthException
     * @deprecated use {@link #extractRequiredParameter(java.util.Map, java.lang.String, java.lang.String) } or
     * {@link java.util.Map#get(java.lang.Object) }
     */
    @Deprecated
    protected static String extractParameter(String response, Pattern regexPattern, boolean required)
            throws OAuthException {
        final Matcher matcher = regexPattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }

        if (required) {
            throw new OAuthException("Response body is incorrect. Can't extract a '" + regexPattern.pattern()
                    + "' from this: '" + response + "'", null);
        }

        return null;
    }

    protected static String extractRequiredParameter(Map<String, String> response, String parameterName,
            String rawResponse) throws OAuthException {
        final String value = response.get(parameterName);

        if (value == null) {
            throw new OAuthException("Response body is incorrect. Can't extract a '" + parameterName
                    + "' from this: '" + rawResponse + "'", null);
        }

        return value;
    }
}
