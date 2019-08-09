package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.OAuth1AccessToken;

public class OAuth1AccessTokenExtractor extends AbstractOAuth1TokenExtractor<OAuth1AccessToken> {

    protected OAuth1AccessTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final OAuth1AccessTokenExtractor INSTANCE = new OAuth1AccessTokenExtractor();
    }

    public static OAuth1AccessTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected OAuth1AccessToken createToken(String token, String secret, String response) {
        return new OAuth1AccessToken(token, secret, response);
    }

}
