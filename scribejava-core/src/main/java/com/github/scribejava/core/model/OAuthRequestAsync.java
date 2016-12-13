package com.github.scribejava.core.model;

import java.io.IOException;

public class OAuthRequestAsync extends AbstractRequest {

    public OAuthRequestAsync(Verb verb, String url) {
        super(verb, url);
    }

    public interface ResponseConverter<T> {

        T convert(Response response) throws IOException;
    }
}
