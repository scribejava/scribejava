package com.github.scribejava.httpclient.okhttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import kotlin.jvm.functions.Function0;
import kotlin.reflect.KClass;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

public class MockCall implements Call {

    private final Collection<Callback> callbacks = new ArrayList<>();
    private boolean canceled;

    @Override
    public void enqueue(Callback responseCallback) {
        callbacks.add(responseCallback);
    }

    @Override
    public void cancel() {
        canceled = true;
        for (Callback callback : callbacks) {
            callback.onFailure(this, new IOException("Canceled"));
        }
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public Request request() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Response execute() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isExecuted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MockCall clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Timeout timeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addEventListener(EventListener arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'addEventListener'");
    }

    @Override
    public <T> T tag(KClass<T> arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'tag'");
    }

    @Override
    public <T> T tag(Class<? extends T> arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'tag'");
    }

    @Override
    public <T> T tag(KClass<T> arg0, Function0<? extends T> arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'tag'");
    }

    @Override
    public <T> T tag(Class<T> arg0, Function0<? extends T> arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'tag'");
    }
}
