package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.ScopeBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * not thread safe
 */
public class AccessTokenRequestParams {

    private final String code;
    private String pkceCodeVerifier;
    private String scope;
    private Map<String, String> extraParameters;

    public AccessTokenRequestParams(String code) {
        this.code = code;
    }

    public static AccessTokenRequestParams create(String code) {
        return new AccessTokenRequestParams(code);
    }

    public AccessTokenRequestParams pkceCodeVerifier(String pkceCodeVerifier) {
        this.pkceCodeVerifier = pkceCodeVerifier;
        return this;
    }

    public AccessTokenRequestParams scope(String scope) {
        this.scope = scope;
        return this;
    }

    public AccessTokenRequestParams scope(ScopeBuilder scope) {
        this.scope = scope.build();
        return this;
    }

    public AccessTokenRequestParams addExtraParameters(Map<String, String> extraParameters) {
        if (extraParameters == null || extraParameters.isEmpty()) {
            return this;
        }
        if (this.extraParameters == null) {
            extraParameters = new HashMap<>();
        }
        this.extraParameters.putAll(extraParameters);
        return this;
    }

    public AccessTokenRequestParams addExtraParameter(String name, String value) {
        if (this.extraParameters == null) {
            extraParameters = new HashMap<>();
        }
        this.extraParameters.put(name, value);
        return this;
    }

    public AccessTokenRequestParams setExtraParameters(Map<String, String> extraParameters) {
        this.extraParameters = extraParameters;
        return this;
    }

    public Map<String, String> getExtraParameters() {
        return extraParameters;
    }

    public String getCode() {
        return code;
    }

    public String getPkceCodeVerifier() {
        return pkceCodeVerifier;
    }

    public String getScope() {
        return scope;
    }
}
