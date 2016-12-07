package com.github.scribejava.core.model;

import com.github.scribejava.core.oauth.OAuthService;
import java.io.IOException;
import java.util.concurrent.Future;

public class OAuthRequestAsync extends AbstractRequest {

    /**
     * @param verb verb
     * @param url url
     * @param service service
     * @deprecated use {@link #OAuthRequestAsync(com.github.scribejava.core.model.Verb, java.lang.String) }
     */
    @Deprecated
    public OAuthRequestAsync(Verb verb, String url, OAuthService<?> service) {
        this(verb, url);
    }

    public OAuthRequestAsync(Verb verb, String url) {
        super(verb, url);
    }

    /**
     * always throws UnsupportedOperationException
     *
     * @param <T> T
     * @param callback callback
     * @param converter converter
     * @return never
     * @deprecated user {@link OAuthService#execute(com.github.scribejava.core.model.OAuthRequestAsync,
     * com.github.scribejava.core.model.OAuthAsyncRequestCallback,
     * com.github.scribejava.core.model.OAuthRequestAsync.ResponseConverter) }
     */
    @Deprecated
    public <T> Future<T> sendAsync(OAuthAsyncRequestCallback<T> callback, ResponseConverter<T> converter) {
        throw new UnsupportedOperationException();
    }

    /**
     * always throws UnsupportedOperationException
     *
     * @param callback callback
     * @return never
     * @deprecated user {@link OAuthService#execute(com.github.scribejava.core.model.OAuthRequestAsync,
     * com.github.scribejava.core.model.OAuthAsyncRequestCallback) }
     */
    @Deprecated
    public Future<Response> sendAsync(OAuthAsyncRequestCallback<Response> callback) {
        throw new UnsupportedOperationException();
    }

    public interface ResponseConverter<T> {

        T convert(Response response) throws IOException;
    }
}
