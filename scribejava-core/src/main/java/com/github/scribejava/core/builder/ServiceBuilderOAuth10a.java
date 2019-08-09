package com.github.scribejava.core.builder;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.OutputStream;

public interface ServiceBuilderOAuth10a extends ServiceBuilderCommon {

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

    ServiceBuilderOAuth10a debugStream(OutputStream debugStream);

    ServiceBuilderOAuth10a debug();

    /**
     * Configures the OAuth 1.0a scope. This is only necessary in some APIs
     *
     * @param scope The OAuth scope
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    ServiceBuilderOAuth10a withScope(String scope);

    OAuth10aService build(DefaultApi10a api);
}
