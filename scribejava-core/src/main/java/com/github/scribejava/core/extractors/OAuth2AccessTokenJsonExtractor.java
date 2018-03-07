package com.github.scribejava.core.extractors;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.Preconditions;

/**
 * JSON (default) implementation of {@link TokenExtractor} for OAuth 2.0
 */
public class OAuth2AccessTokenJsonExtractor implements TokenExtractor<OAuth2AccessToken> {

    private static final Pattern ACCESS_TOKEN_REGEX_PATTERN = Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern TOKEN_TYPE_REGEX_PATTERN = Pattern.compile("\"token_type\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern EXPIRES_IN_REGEX_PATTERN = Pattern.compile("\"expires_in\"\\s*:\\s*\"?(\\d*?)\"?\\D");
    private static final Pattern REFRESH_TOKEN_REGEX_PATTERN = Pattern.compile("\"refresh_token\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern SCOPE_REGEX_PATTERN = Pattern.compile("\"scope\"\\s*:\\s*\"([^\"]*?)\"");
    private static final Pattern ERROR_REGEX_PATTERN = Pattern.compile("\"error\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern ERROR_DESCRIPTION_REGEX_PATTERN
            = Pattern.compile("\"error_description\"\\s*:\\s*\"([^\"]*?)\"");
    private static final Pattern ERROR_URI_REGEX_PATTERN = Pattern.compile("\"error_uri\"\\s*:\\s*\"(\\S*?)\"");

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
     * @param response response
     */
    public void generateError(String response) {
        final String errorInString = extractParameter(response, ERROR_REGEX_PATTERN, true);
        final String errorDescription = extractParameter(response, ERROR_DESCRIPTION_REGEX_PATTERN, false);
        final String errorUriInString = extractParameter(response, ERROR_URI_REGEX_PATTERN, false);
        URI errorUri;
        try {
            errorUri = errorUriInString == null ? null : URI.create(errorUriInString);
        } catch (IllegalArgumentException iae) {
            errorUri = null;
        }

        OAuth2AccessTokenErrorResponse.ErrorCode errorCode;
        try {
            errorCode = OAuth2AccessTokenErrorResponse.ErrorCode.valueOf(errorInString);
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorDescription, errorUri, response);
    }

    private OAuth2AccessToken createToken(String response) {
        final String accessToken = extractParameter(response, ACCESS_TOKEN_REGEX_PATTERN, true);
        final String tokenType = extractParameter(response, TOKEN_TYPE_REGEX_PATTERN, false);
        final String expiresInString = extractParameter(response, EXPIRES_IN_REGEX_PATTERN, false);
        Integer expiresIn;
        try {
            expiresIn = expiresInString == null ? null : Integer.valueOf(expiresInString);
        } catch (NumberFormatException nfe) {
            expiresIn = null;
        }
        final String refreshToken = extractParameter(response, REFRESH_TOKEN_REGEX_PATTERN, false);
        final String scope = extractParameter(response, SCOPE_REGEX_PATTERN, false);

        return createToken(accessToken, tokenType, expiresIn, refreshToken, scope, response);
    }

    protected OAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, String response) {
        return new OAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, response);
    }

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
}
