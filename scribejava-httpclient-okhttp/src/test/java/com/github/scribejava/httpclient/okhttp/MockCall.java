package com.github.scribejava.httpclient.okhttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

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
    public Call clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
