package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.OAuth1AccessToken;

public class OAuth1AccessTokenJSONExtractor extends AbstractOAuth1JSONTokenExtractor<OAuth1AccessToken> {

    protected OAuth1AccessTokenJSONExtractor() {
    }

    @Override
    protected OAuth1AccessToken createToken(String token, String secret, String response) {
        return new OAuth1AccessToken(token, secret, response);
    }

    private static class InstanceHolder {

        private static final OAuth1AccessTokenJSONExtractor INSTANCE = new OAuth1AccessTokenJSONExtractor();
    }

    public static OAuth1AccessTokenJSONExtractor instance() {
        return InstanceHolder.INSTANCE;
    }
}
