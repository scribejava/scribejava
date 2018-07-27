package com.github.scribejava.httpclient.okhttp;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * the only reason, this is a dynamic proxy is, that checkstyle forbids implementing clone()!
 */
public class MockCall implements InvocationHandler {

    private final Collection<Callback> callbacks;
    private boolean canceled;

    public MockCall() {
        this(new ArrayList<Callback>());
    }

    private MockCall(Collection<Callback> callbacks) {
        this.callbacks = new ArrayList<>(callbacks);
    }

    public void enqueue(Callback responseCallback) {
        callbacks.add(responseCallback);
    }

    public void cancel(Call proxy) {
        canceled = true;
        for (Callback callback : callbacks) {
            callback.onFailure(proxy, new IOException("Canceled"));
        }
    }

    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
        case "enqueue":
            enqueue((Callback) args[0]);
            return null;
        case "cancel":
            cancel((Call) proxy);
            return null;
        case "isCanceled":
            return isCanceled();
        default:
            throw new UnsupportedOperationException(method.toString());
        }
    }

    /**
     * @return
     */
    public static Call create() {
        return (Call) Proxy.newProxyInstance(Call.class.getClassLoader(), new Class[] { Call.class }, new MockCall());
    }
}
