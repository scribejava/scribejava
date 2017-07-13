package com.github.scribejava.apis.google;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import java.util.regex.Pattern;

/**
 *
 * @deprecated use generic {@link OpenIdJsonTokenExtractor}
 */
@Deprecated
public class GoogleJsonTokenExtractor extends OpenIdJsonTokenExtractor {

    private static final Pattern ID_TOKEN_REGEX_PATTERN = Pattern.compile("\"id_token\"\\s*:\\s*\"(\\S*?)\"");

    protected GoogleJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final GoogleJsonTokenExtractor INSTANCE = new GoogleJsonTokenExtractor();
    }

    public static GoogleJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected GoogleToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, String response) {
        return new GoogleToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, ID_TOKEN_REGEX_PATTERN, false), response);
    }
}
