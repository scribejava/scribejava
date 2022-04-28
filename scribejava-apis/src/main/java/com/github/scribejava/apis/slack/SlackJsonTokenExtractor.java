package com.github.scribejava.apis.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.Preconditions;

import java.io.IOException;

public class SlackJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    protected SlackJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final SlackJsonTokenExtractor INSTANCE = new SlackJsonTokenExtractor();
    }

    public static SlackJsonTokenExtractor instance() {
        return SlackJsonTokenExtractor.InstanceHolder.INSTANCE;
    }

    @Override
    public OAuth2AccessToken extract(Response response) throws IOException {
        final String body = response.getBody();
        Preconditions.checkEmptyString(body,
                "Response body is incorrect. Can't extract a token from an empty string");

        if (response.getCode() != 200) {
            generateError(response);
        }
        return createToken(body);
    }

    // Overridden private method
    private OAuth2AccessToken createToken(String rawResponse) throws IOException {

        final JsonNode response = OBJECT_MAPPER.readTree(rawResponse);

        final JsonNode expiresInNode = response.get("expires_in");
        final JsonNode refreshToken = response.get(OAuthConstants.REFRESH_TOKEN);
        final JsonNode scope = response.get(OAuthConstants.SCOPE);
        final JsonNode tokenType = response.get("token_type");
        final JsonNode globalAccessToken = response.get(OAuthConstants.ACCESS_TOKEN);

        return createToken( //
                globalAccessToken == null ? "" : globalAccessToken.asText(), // Avoid "access_token can't be null"
                tokenType == null ? null : tokenType.asText(),
                expiresInNode == null ? null : expiresInNode.asInt(),
                refreshToken == null ? null : refreshToken.asText(),
                scope == null ? null : scope.asText(), response, rawResponse);
    }

    @Override
    protected SlackOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JsonNode response, String rawResponse) {
        final String userAccessToken;
        final JsonNode userAccessTokenNode = response.get("authed_user").get("access_token");
        if (userAccessTokenNode == null) {
            userAccessToken = "";
        } else {
            userAccessToken = userAccessTokenNode.asText();
        }

        return new SlackOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, userAccessToken,
                rawResponse);
    }
}
