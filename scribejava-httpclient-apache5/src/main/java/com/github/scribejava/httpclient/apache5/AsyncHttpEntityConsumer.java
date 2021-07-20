package com.github.scribejava.httpclient.apache5;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.nio.support.classic.AbstractClassicEntityConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

/**
 * Compatibility with classic HttpClient API:
 * Consumes the async response as an input stream and creates an {@link HttpEntity}.
 */
public class AsyncHttpEntityConsumer extends AbstractClassicEntityConsumer<HttpEntity> {

    public AsyncHttpEntityConsumer(int initialBufferSize, Executor executor) {
        super(initialBufferSize, executor);
    }

    @Override
    protected HttpEntity consumeData(ContentType contentType, InputStream inputStream) throws IOException {
        return new InputStreamEntity(inputStream, contentType);
    }
}
