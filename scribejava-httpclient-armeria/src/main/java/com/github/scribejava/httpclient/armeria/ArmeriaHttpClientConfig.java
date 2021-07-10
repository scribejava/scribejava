package com.github.scribejava.httpclient.armeria;

import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.ClientOptions;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.client.retry.RetryingClient;
import com.linecorp.armeria.common.SessionProtocol;
import java.util.function.Function;

public class ArmeriaHttpClientConfig implements HttpClientConfig {

    private static final SessionProtocol DEFAULT_PROTOCOL_PREFERENCE = SessionProtocol.H1; // H1 or H2

    private final ClientOptions clientOptions;
    private final ClientFactory clientFactory;
    private SessionProtocol protocolPreference;
    private Function<? super HttpClient, RetryingClient> retry;
    private Function<? super HttpClient, LoggingClient> logging;

    /**
     * Creates new {@link ArmeriaHttpClientConfig} using provided {@link ClientOptions} and {@link ClientFactory}.
     *
     * @param clientOptions clientOptions
     * @param clientFactory clientFactory
     */
    public ArmeriaHttpClientConfig(ClientOptions clientOptions, ClientFactory clientFactory) {
        this.clientOptions = clientOptions;
        this.clientFactory = clientFactory;
        protocolPreference = DEFAULT_PROTOCOL_PREFERENCE;
    }

    /**
     * Creates new {@link HttpClientConfig} using default settings.
     *
     * @return new {@link HttpClientConfig} using default settings.
     */
    @Override
    public HttpClientConfig createDefaultConfig() {
        return defaultConfig();
    }

    /**
     * Creates new {@link ArmeriaHttpClientConfig} using default settings.
     *
     * @return ArmeriaHttpClientConfig
     */
    public static ArmeriaHttpClientConfig defaultConfig() {
        return new ArmeriaHttpClientConfig(null, null);
    }

    /**
     * Selects which protocol shall take preference when generic protocol scheme used by the URL, like {@code http} or
     * {@code https}.
     *
     * @param protocolPreference specifies which protocol shall take preference. Acceptable values:
     * {@link SessionProtocol#H1} and {@link SessionProtocol#H2}
     */
    public void setProtocolPreference(SessionProtocol protocolPreference) {
        if (protocolPreference != SessionProtocol.H1 && protocolPreference != SessionProtocol.H2) {
            throw new IllegalArgumentException("Invalid protocolPreference: " + protocolPreference);
        }
        this.protocolPreference = protocolPreference;
    }

    public ArmeriaHttpClientConfig withProtocolPreference(SessionProtocol protocolPreference) {
        setProtocolPreference(protocolPreference);
        return this;
    }

    public void setRetry(Function<? super HttpClient, RetryingClient> retry) {
        this.retry = retry;
    }

    public ArmeriaHttpClientConfig withRetry(Function<? super HttpClient, RetryingClient> retry) {
        this.retry = retry;
        return this;
    }

    public void setLogging(Function<? super HttpClient, LoggingClient> logging) {
        this.logging = logging;
    }

    public ArmeriaHttpClientConfig withLogging(Function<? super HttpClient, LoggingClient> logging) {
        this.logging = logging;
        return this;
    }

    ArmeriaWebClientBuilder createClientBuilder() {
        return new ArmeriaWebClientBuilder(clientOptions, clientFactory, protocolPreference, retry, logging);
    }
}
