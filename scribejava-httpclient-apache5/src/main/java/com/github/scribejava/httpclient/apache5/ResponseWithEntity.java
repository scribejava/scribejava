package com.github.scribejava.httpclient.apache5;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;

class ResponseWithEntity {

    private final HttpResponse response;
    private final HttpEntity entity;

    ResponseWithEntity(HttpResponse response, HttpEntity entity) {
        this.response = response;
        this.entity = entity;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public HttpEntity getEntity() {
        return entity;
    }
}
