package com.github.scribejava.apis.constantcontact;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstantContactTokenExtractor implements TokenExtractor<OAuth2AccessToken> {

    private static final String REGEXP = "\"access_token\"\\s*:\\s*\"([^&\"]+)\"";

    protected ConstantContactTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final ConstantContactTokenExtractor INSTANCE = new ConstantContactTokenExtractor();
    }

    public static ConstantContactTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public OAuth2AccessToken extract(String response) {
        Preconditions.checkEmptyString(response,
                "Response body is incorrect. Can't extract a token from an empty string");

        final Matcher matcher = Pattern.compile(REGEXP).matcher(response);
        if (matcher.find()) {
            final String token = OAuthEncoder.decode(matcher.group(1));
            return new OAuth2AccessToken(token, response);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '"
                    + response + "'", null);
        }
    }
}
