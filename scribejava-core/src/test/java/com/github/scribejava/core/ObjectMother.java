package com.github.scribejava.core;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;

public class ObjectMother {

    public static OAuthRequest createSampleOAuthRequest() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com", new OAuth20ServiceImpl(null, new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort80() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:80", new OAuth20ServiceImpl(null, new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort80v2() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:80/test", new OAuth20ServiceImpl(null, new OAuthConfig(
                "test", "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort8080() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:8080", new OAuth20ServiceImpl(null, new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort443() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com:443", new OAuth20ServiceImpl(null, new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort443v2() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com:443/test", new OAuth20ServiceImpl(null, new OAuthConfig(
                "test", "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }
}
