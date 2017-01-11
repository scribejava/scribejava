package com.github.scribejava.core.model;

/**
 *
 * @deprecated use {@link OAuthRequest}
 */
@Deprecated
public class OAuthRequestAsync extends OAuthRequest {

    /**
     *
     * @deprecated use {@link OAuthRequest#OAuthRequest(com.github.scribejava.core.model.Verb, java.lang.String) }
     */
    @Deprecated
    public OAuthRequestAsync(Verb verb, String url) {
        super(verb, url);
    }

    /**
     *
     * @param <T> goal type
     * @deprecated use {@link OAuthRequest.ResponseConverter}
     */
    @Deprecated
    public interface ResponseConverter<T> extends OAuthRequest.ResponseConverter<T> {
    }
}
