package com.github.scribejava.core.builder;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuthService;
import java.io.OutputStream;

/**
 * Implementation of the Builder pattern, with a fluent interface that creates a {@link OAuthService}
 */
public interface ServiceBuilderCommon {

    /**
     * Adds an OAuth callback url
     *
     * @param callback callback url. Must be a valid url or 'oob'
     * ({@link com.github.scribejava.core.model.OAuthConstants#OOB} for out of band OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    ServiceBuilderCommon callback(String callback);

    /**
     * Configures the api key
     *
     * @param apiKey The api key for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    ServiceBuilderCommon apiKey(String apiKey);

    /**
     * Configures the api secret
     *
     * @param apiSecret The api secret for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    ServiceBuilderCommon apiSecret(String apiSecret);

    ServiceBuilderCommon httpClientConfig(HttpClientConfig httpClientConfig);

    /**
     * takes precedence over httpClientConfig
     *
     * @param httpClient externally created HTTP client
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    ServiceBuilderCommon httpClient(HttpClient httpClient);

    ServiceBuilderCommon userAgent(String userAgent);

    ServiceBuilderCommon debugStream(OutputStream debugStream);

    ServiceBuilderCommon debug();
}
