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

    private static final String ACCESS_TOKEN_REGEX = "\"access_token\"\\s*:\\s*\"(\\S*?)\"";
    private static final String TOKEN_TYPE_REGEX = "\"token_type\"\\s*:\\s*\"(\\S*?)\"";
    private static final String EXPIRES_IN_REGEX = "\"expires_in\"\\s*:\\s*\"?(\\d*?)\"?\\D";
    private static final String REFRESH_TOKEN_REGEX = "\"refresh_token\"\\s*:\\s*\"(\\S*?)\"";
    private static final String SCOPE_REGEX = "\"scope\"\\s*:\\s*\"(\\S*?)\"";
    private static final String ERROR_REGEX = "\"error\"\\s*:\\s*\"(\\S*?)\"";
    private static final String ERROR_DESCRIPTION_REGEX = "\"error_description\"\\s*:\\s*\"([^\"]*?)\"";
    private static final String ERROR_URI_REGEX = "\"error_uri\"\\s*:\\s*\"(\\S*?)\"";

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
        Preconditions.checkEmptyString(body,
                "Response body is incorrect. Can't extract a token from an empty string");

        if (response.getCode() != 200) {
            generateError(response.getBody());
        }
        return createToken(body);
    }

    /**
     * Related documentation: https://tools.ietf.org/html/rfc6749#section-5.2
     */
    private static void generateError(String response) {
        final String errorInString = extractParameter(response, ERROR_REGEX, true);
        final String errorDescription = extractParameter(response, ERROR_DESCRIPTION_REGEX, false);
        final String errorUriInString = extractParameter(response, ERROR_URI_REGEX, false);
        URI errorUri;
        try {
            errorUri = errorUriInString == null ? null : URI.create(errorUriInString);
        } catch (IllegalArgumentException iae) {
            errorUri = null;
        }

        throw new OAuth2AccessTokenErrorResponse(OAuth2AccessTokenErrorResponse.ErrorCode.valueOf(errorInString),
                errorDescription, errorUri, response);
    }

    private OAuth2AccessToken createToken(String response) {
        final String accessToken = extractParameter(response, ACCESS_TOKEN_REGEX, true);
        final String tokenType = extractParameter(response, TOKEN_TYPE_REGEX, false);
        final String expiresInString = extractParameter(response, EXPIRES_IN_REGEX, false);
        Integer expiresIn;
        try {
            expiresIn = expiresInString == null ? null : Integer.valueOf(expiresInString);
        } catch (NumberFormatException nfe) {
            expiresIn = null;
        }
        final String refreshToken = extractParameter(response, REFRESH_TOKEN_REGEX, false);
        final String scope = extractParameter(response, SCOPE_REGEX, false);

        return createToken(accessToken, tokenType, expiresIn, refreshToken, scope, response);
    }

    protected OAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, String response) {
        return new OAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, response);
    }

    protected static String extractParameter(String response, String regex, boolean required) throws OAuthException {
        final Matcher matcher = Pattern.compile(regex).matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }

        if (required) {
            throw new OAuthException("Response body is incorrect. Can't extract a '" + regex
                    + "' from this: '" + response + "'", null);
        }

        return null;
    }
}
