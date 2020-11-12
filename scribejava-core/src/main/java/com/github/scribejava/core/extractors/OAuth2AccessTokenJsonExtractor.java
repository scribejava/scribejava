package com.github.scribejava.core.extractors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth2.OAuth2Error;
import com.github.scribejava.core.utils.Preconditions;

/**
 * JSON (default) implementation of {@link TokenExtractor} for OAuth 2.0
 */
public class OAuth2AccessTokenJsonExtractor extends AbstractJsonExtractor implements TokenExtractor<OAuth2AccessToken> {

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
            generateError(response);
        }
        return createToken(body);
    }

    /**
     *
     * @param rawResponse rawResponse
     * @throws java.io.IOException IOException
     * @deprecated use {@link #generateError(com.github.scribejava.core.model.Response) }
     */
    @Deprecated
    public void generateError(String rawResponse) throws IOException {
        generateError(new Response(-1, null, null, rawResponse));
    }

    /**
     * Related documentation: https://tools.ietf.org/html/rfc6749#section-5.2
     *
     * @param response response
     * @throws java.io.IOException IOException
     *
     */
    public void generateError(Response response) throws IOException {
        final String responseBody = response.getBody();
        final JsonNode responseBodyJson;
        try {
            responseBodyJson = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException ex) {
            throw new OAuth2AccessTokenErrorResponse(null, null, null, response);
        }

        final JsonNode errorUriInString = responseBodyJson.get("error_uri");
        URI errorUri;
        try {
            errorUri = errorUriInString == null ? null : URI.create(errorUriInString.asText());
        } catch (IllegalArgumentException iae) {
            errorUri = null;
        }

        OAuth2Error errorCode;
        try {
            errorCode = OAuth2Error
                    .parseFrom(extractRequiredParameter(responseBodyJson, "error", responseBody).asText());
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        final JsonNode errorDescription = responseBodyJson.get("error_description");

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorDescription == null ? null : errorDescription.asText(),
                errorUri, response);
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
        return new OAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
    }
}
