package com.github.scribejava.httpclient.okhttp;

import okhttp3.Request;

/**
 * Allows interception of the request building process, e.g. to add custom headers or tags.
 */
public interface OkHttpRequestDecorator {

    void decorate(Request.Builder requestBuilder);
}
