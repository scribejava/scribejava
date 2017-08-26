package com.github.scribejava.apis.ucoz;

import com.github.scribejava.core.model.OAuth1RequestToken;

public class OAuth1RequestUcozTokenExtractor extends AbstractOauth1UcozTokenExtractor<OAuth1RequestToken> {

    protected OAuth1RequestUcozTokenExtractor() {
    }

    @Override
    protected OAuth1RequestToken createToken(String token, String secret, String response) {
        return new OAuth1RequestToken(token, secret, response);
    }

    private static class InstanceHolder {

        private static final OAuth1RequestUcozTokenExtractor INSTANCE = new OAuth1RequestUcozTokenExtractor();
    }

    public static OAuth1RequestUcozTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }
}