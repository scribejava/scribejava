package com.github.scribejava.core.builder;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.OutputStream;

public interface ServiceBuilderOAuth10a extends ServiceBuilderCommon {

    @Override
    ServiceBuilderOAuth10a callback(String callback);

    @Override
    ServiceBuilderOAuth10a apiKey(String apiKey);

    @Override
    ServiceBuilderOAuth10a apiSecret(String apiSecret);

    @Override
    ServiceBuilderOAuth10a httpClientConfig(HttpClientConfig httpClientConfig);

    @Override
    ServiceBuilderOAuth10a httpClient(HttpClient httpClient);

    @Override
    ServiceBuilderOAuth10a userAgent(String userAgent);

    @Override
    ServiceBuilderOAuth10a debugStream(OutputStream debugStream);

    @Override
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
