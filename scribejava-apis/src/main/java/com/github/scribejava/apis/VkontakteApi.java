package com.github.scribejava.apis;

import com.github.scribejava.apis.vk.VKJsonTokenExtractor;
import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class VkontakteApi extends DefaultApi20 {

    public static final String VERSION = "5.73";

    protected VkontakteApi() {
    }

    private static class InstanceHolder {

        private static final VkontakteApi INSTANCE = new VkontakteApi();
    }

    public static VkontakteApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth.vk.com/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://oauth.vk.com/authorize?v=" + VERSION;
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }

    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.REQUEST_BODY;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return VKJsonTokenExtractor.instance();
    }
}
