package org.scribe.builder.api;

import org.scribe.model.Token;
import org.scribe.model.TransmissionType;
import org.scribe.model.Verb;


public class SapoApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://id.sapo.pt/oauth/authorize?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://id.sapo.pt/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://id.sapo.pt/oauth/request_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public TransmissionType getTransmissionType() {
        return TransmissionType.URI;
    }
}
