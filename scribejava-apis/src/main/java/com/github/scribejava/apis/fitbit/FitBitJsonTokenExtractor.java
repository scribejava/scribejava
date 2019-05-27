package com.github.scribejava.apis.fitbit;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.oauth2.OAuth2Error;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
            String refreshToken, String scope, Map<String, String> response, String rawResponse) {
        return new FitBitOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                response.get("user_id"), rawResponse);
    }

    /**
     * Related documentation: https://dev.fitbit.com/build/reference/web-api/oauth2/
     */
    @Override
    public void generateError(String rawResponse) throws IOException {
        @SuppressWarnings("unchecked")
        final Map<String, List<Map<String, String>>> response = OAuth2AccessTokenJsonExtractor.OBJECT_MAPPER
                .readValue(rawResponse, Map.class);

        final Map<String, String> errorResponse = response.get("errors").get(0);
        OAuth2Error errorCode;
        try {
            errorCode = OAuth2Error.parseFrom(extractRequiredParameter(errorResponse, "errorType", rawResponse));
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorResponse.get("message"), null, rawResponse);
    }
}
