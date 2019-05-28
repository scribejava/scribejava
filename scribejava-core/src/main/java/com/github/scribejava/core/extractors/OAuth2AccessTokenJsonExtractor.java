package com.github.scribejava.core.extractors;

import com.fasterxml.jackson.databind.JsonNode;
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
        final JsonNode response = OBJECT_MAPPER.readTree(rawResponse);

        final JsonNode errorUriInString = response.get("error_uri");
        URI errorUri;
        try {
            errorUri = errorUriInString == null ? null : URI.create(errorUriInString.asText());
        } catch (IllegalArgumentException iae) {
            errorUri = null;
        }

        OAuth2Error errorCode;
        try {
            errorCode = OAuth2Error.parseFrom(extractRequiredParameter(response, "error", rawResponse).asText());
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        final JsonNode errorDescription = response.get("error_description");

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorDescription == null ? null : errorDescription.asText(),
                errorUri, rawResponse);
    }

    private OAuth2AccessToken createToken(String rawResponse) throws IOException {

        final JsonNode response = OBJECT_MAPPER.readTree(rawResponse);

        final JsonNode expiresInNode = response.get("expires_in");
        final JsonNode refreshToken = response.get(OAuthConstants.REFRESH_TOKEN);
        final JsonNode scope = response.get(OAuthConstants.SCOPE);
        final JsonNode tokenType = response.get("token_type");

        return createToken(extractRequiredParameter(response, OAuthConstants.ACCESS_TOKEN, rawResponse).asText(),
                tokenType == null ? null : tokenType.asText(), expiresInNode == null ? null : expiresInNode.asInt(),
                refreshToken == null ? null : refreshToken.asText(), scope == null ? null : scope.asText(), response,
                rawResponse);
    }

    protected OAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JsonNode response, String rawResponse) {
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
     * java.lang.String, com.fasterxml.jackson.databind.JsonNode, java.lang.String)}
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
     * @deprecated use {@link #extractRequiredParameter(com.fasterxml.jackson.databind.JsonNode, java.lang.String,
     * java.lang.String)}
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

    protected static JsonNode extractRequiredParameter(JsonNode errorNode, String parameterName, String rawResponse)
            throws OAuthException {
        final JsonNode value = errorNode.get(parameterName);

        if (value == null) {
            throw new OAuthException("Response body is incorrect. Can't extract a '" + parameterName
                    + "' from this: '" + rawResponse + "'", null);
        }

        return value;
    }
}
