package com.github.scribejava.apis.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

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
    protected SlackOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
                                                  String refreshToken, String scope, JsonNode response, String rawResponse) {
        String userAccessToken = "";
        final JsonNode userAccessTokenNode = response.get("authed_user").get("access_token");
        if (userAccessTokenNode != null) {
           userAccessToken = userAccessTokenNode.asText();
        }

        return new SlackOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                userAccessToken, rawResponse);
    }
}
