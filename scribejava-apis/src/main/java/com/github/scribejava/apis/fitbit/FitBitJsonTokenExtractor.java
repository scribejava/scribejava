package com.github.scribejava.apis.fitbit;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;

import java.util.regex.Pattern;

public class FitBitJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern USER_ID_REGEX_PATTERN = Pattern.compile("\"user_id\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern ERROR_REGEX_PATTERN = Pattern.compile("\"errorType\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern ERROR_DESCRIPTION_REGEX_PATTERN = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]*?)\"");
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
            String refreshToken, String scope, String response) {
        return new FitBitOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, USER_ID_REGEX_PATTERN, false), response);
    }

    /**
     * Related documentation: https://dev.fitbit.com/build/reference/web-api/oauth2/
     */
    @Override
    public void generateError(String response) {
        final String errorInString = extractParameter(response, ERROR_REGEX_PATTERN, true);
        final String errorDescription = extractParameter(response, ERROR_DESCRIPTION_REGEX_PATTERN, false);

        OAuth2AccessTokenErrorResponse.ErrorCode errorCode;
        try {
            errorCode = OAuth2AccessTokenErrorResponse.ErrorCode.valueOf(errorInString);
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorDescription, null, response);
    }
}
