package com.github.scribejava.apis;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class LinkedInApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://api.linkedin.com/uas/oauth/authenticate?oauth_token=%s";
    private static final String REQUEST_TOKEN_URL = "https://api.linkedin.com/uas/oauth/requestToken";

    private static class InstanceHolder {
        private static final LinkedInApi INSTANCE = new LinkedInApi();
    }

    public static LinkedInApi instance() {
        return InstanceHolder.INSTANCE;
    }

    private final Set<String> scopes;

    public LinkedInApi() {
        scopes = Collections.emptySet();
    }

    public LinkedInApi(final Set<String> scopes) {
        this.scopes = Collections.unmodifiableSet(scopes);
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.linkedin.com/uas/oauth/accessToken";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return scopes.isEmpty() ? REQUEST_TOKEN_URL : REQUEST_TOKEN_URL + "?scope=" + scopesAsString();
    }

    private String scopesAsString() {
        final StringBuilder builder = new StringBuilder();
        for (final String scope : scopes) {
            builder.append("+" + scope);
        }
        return builder.substring(1);
    }

    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    public static LinkedInApi withScopes(final String... scopes) {
        final Set<String> scopeSet = new HashSet<>(Arrays.asList(scopes));
        return new LinkedInApi(scopeSet);
    }

}
