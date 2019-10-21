package com.github.scribejava.apis.polar;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.oauth2.OAuth2Error;

import java.io.IOException;

/**
 * Token related documentation: https://www.polar.com/accesslink-api/#token-endpoint
 */
public class PolarJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    protected PolarJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final PolarJsonTokenExtractor INSTANCE = new PolarJsonTokenExtractor();
    }

    public static PolarJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected PolarOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JsonNode response, String rawResponse) {
        return new PolarOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                response.get("x_user_id").asText(), rawResponse);
    }

    @Override
    public void generateError(String rawResponse) throws IOException {
        final JsonNode errorNode = OAuth2AccessTokenJsonExtractor.OBJECT_MAPPER.readTree(rawResponse)
                .get("errors").get(0);

        OAuth2Error errorCode;
        try {
            errorCode = OAuth2Error.parseFrom(extractRequiredParameter(errorNode, "errorType", rawResponse).asText());
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorNode.get("message").asText(), null, rawResponse);
    }
}
