package com.github.scribejava.core.model;

public class OAuthRequest extends AbstractRequest {

    public OAuthRequest(Verb verb, String url) {
        super(verb, url);
    }

    /**
     *
     * @param verb verb
     * @param url url
     * @param config unused
     * @deprecated use {@link #OAuthRequest(com.github.scribejava.core.model.Verb, java.lang.String) }
     */
    @Deprecated
    public OAuthRequest(Verb verb, String url, OAuthConfig config) {
        this(verb, url);
    }
}
