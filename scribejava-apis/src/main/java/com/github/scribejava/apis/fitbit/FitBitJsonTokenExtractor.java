package com.github.scribejava.apis.fitbit;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.oauth2.OAuth2Error;
import java.io.IOException;

public class FitBitJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    protected FitBitJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final FitBitJsonTokenExtractor INSTANCE = new FitBitJsonTokenExtractor();
    }

    public static FitBitJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected FitBitOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JsonNode response, String rawResponse) {
        return new FitBitOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                response.get("user_id").asText(), rawResponse);
    }

    /**
     * Related documentation: https://dev.fitbit.com/build/reference/web-api/oauth2/
     */
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
