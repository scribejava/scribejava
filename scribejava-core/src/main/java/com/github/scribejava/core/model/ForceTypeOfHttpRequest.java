package com.github.scribejava.core.model;

public enum ForceTypeOfHttpRequest {

    NONE,
    FORCE_ASYNC_ONLY_HTTP_REQUESTS,
    FORCE_SYNC_ONLY_HTTP_REQUESTS,
    PREFER_ASYNC_ONLY_HTTP_REQUESTS,
    PREFER_SYNC_ONLY_HTTP_REQUESTS
}
