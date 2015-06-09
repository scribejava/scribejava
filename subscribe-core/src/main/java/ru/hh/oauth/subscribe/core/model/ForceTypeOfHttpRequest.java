package ru.hh.oauth.subscribe.core.model;

public enum ForceTypeOfHttpRequest {

    NONE,
    FORCE_ASYNC_ONLY_HTTP_REQUESTS,
    FORCE_SYNC_ONLY_HTTP_REQUESTS,
    PREFER_ASYNC_ONLY_HTTP_REQUESTS,
    PREFER_SYNC_ONLY_HTTP_REQUESTS
}
