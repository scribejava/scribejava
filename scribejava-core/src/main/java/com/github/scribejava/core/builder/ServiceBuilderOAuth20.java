package com.github.scribejava.core.builder;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

public interface ServiceBuilderOAuth20 extends ServiceBuilderCommon {

    @Override
    ServiceBuilderOAuth20 callback(String callback);

    @Override
    ServiceBuilderOAuth20 apiKey(String apiKey);

    @Override
    ServiceBuilderOAuth20 apiSecret(String apiSecret);

    @Override
    ServiceBuilderOAuth20 httpClientConfig(HttpClientConfig httpClientConfig);

    @Override
    ServiceBuilderOAuth20 httpClient(HttpClient httpClient);

    @Override
    ServiceBuilderOAuth20 userAgent(String userAgent);

    ServiceBuilderOAuth20 responseType(String responseType);

    /**
     * Configures the default OAuth 2.0 scope.<br>
     *
     * You can request any uniq scope per each access token request using
     * {@link com.github.scribejava.core.oauth.AuthorizationUrlBuilder#scope(java.lang.String) }.<br><br>
     *
     * In case you're requesting always the same scope,<br>
     * you can just set it here and do not provide scope param nowhere more.
     *
     * @param defaultScope The OAuth scope, used as deafult
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    ServiceBuilderOAuth20 defaultScope(String defaultScope);

    OAuth20Service build(DefaultApi20 api);
}
