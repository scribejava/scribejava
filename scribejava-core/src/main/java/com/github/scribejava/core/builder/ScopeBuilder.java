package com.github.scribejava.core.builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2.0 Scope Builder. It allows specifying multiple scopes one by one. It will combine them in the single
 * space-delimited string. OAuth 2.0 standard specifies space as a delimiter for scopes
 * (https://tools.ietf.org/html/rfc6749#section-3.3). If you found API, that do not support spaces, but support
 * something else, let ScribeJava know (submit the issue here https://github.com/scribejava/scribejava/issues) and use
 * your own concatenated string as a temporary workaround.
 */
public class ScopeBuilder {

    private final Set<String> scopes = new HashSet<>();

    public ScopeBuilder() {
    }

    public ScopeBuilder(String scope) {
        withScope(scope);
    }

    public ScopeBuilder(String... scopes) {
        withScopes(scopes);
    }

    public ScopeBuilder(Collection<String> scopes) {
        withScopes(scopes);
    }

    public final ScopeBuilder withScope(String scope) {
        scopes.add(scope);
        return this;
    }

    public final ScopeBuilder withScopes(String... scopes) {
        this.scopes.addAll(Arrays.asList(scopes));
        return this;
    }

    public final ScopeBuilder withScopes(Collection<String> scopes) {
        this.scopes.addAll(scopes);
        return this;
    }

    public String build() {
        final StringBuilder scopeBuilder = new StringBuilder();
        for (String scope : scopes) {
            scopeBuilder.append(' ').append(scope);
        }
        return scopeBuilder.substring(1);
    }
}
