package com.github.scribejava.httpclient.apache5;

import org.apache.hc.core5.function.Supplier;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.AbstractAsyncResponseConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Implementation of an {@link AsyncEntityConsumer}
 * Produces a {@link ResponseWithEntity} containing the HTTP response and entity.
 */
public class ResponseWithEntityConsumer extends AbstractAsyncResponseConsumer<ResponseWithEntity, HttpEntity> {

    public ResponseWithEntityConsumer(Supplier<AsyncEntityConsumer<HttpEntity>> dataConsumerSupplier) {
        super(dataConsumerSupplier);
    }

    public ResponseWithEntityConsumer(AsyncEntityConsumer<HttpEntity> dataConsumer) {
        super(dataConsumer);
    }

    @Override
    protected ResponseWithEntity buildResult(HttpResponse response, HttpEntity entity, ContentType contentType) {
        return new ResponseWithEntity(response, entity);
    }

    @Override
    public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {
    }
}
