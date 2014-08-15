package org.scribe.test.helpers;

import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuth20ServiceImpl;

public class ObjectMother {

    public static OAuthRequest createSampleOAuthRequest() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com", new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort80() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:80", new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort80_2() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:80/test", new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig(
                "test", "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort8080() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:8080", new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort443() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com:443", new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig("test",
                "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }

    public static OAuthRequest createSampleOAuthRequestPort443_2() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com:443/test", new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig(
                "test", "test")));
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
        request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
        return request;
    }
}
