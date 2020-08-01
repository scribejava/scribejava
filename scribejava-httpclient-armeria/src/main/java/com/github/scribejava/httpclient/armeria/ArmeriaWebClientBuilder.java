package com.github.scribejava.httpclient.armeria;

import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.ClientOptions;
import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.WebClientBuilder;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.client.retry.RetryingClient;
import com.linecorp.armeria.common.SessionProtocol;
import java.util.function.Function;

/**
 * A builder of {@link WebClient} using supplied configuration parameters.
 */
public class ArmeriaWebClientBuilder {

    private final ClientFactory clientFactory;
    private final ClientOptions clientOptions;
    private final SessionProtocol protocolPreference;
    private final Function<? super HttpClient, RetryingClient> retry;
    private final Function<? super HttpClient, LoggingClient> logging;

    ArmeriaWebClientBuilder(ClientOptions clientOptions, ClientFactory clientFactory,
            SessionProtocol protocolPreference, Function<? super HttpClient, RetryingClient> retry,
            Function<? super HttpClient, LoggingClient> logging) {
        this.clientOptions = clientOptions;
        this.clientFactory = clientFactory;
        this.protocolPreference = protocolPreference;
        this.retry = retry;
        this.logging = logging;
    }

    WebClient newWebClient(String scheme, String authority) {
        final SessionProtocol protocol = protocol(scheme);
        final Endpoint endpoint = Endpoint.parse(authority);
        final WebClientBuilder clientBuilder = WebClient.builder(protocol, endpoint);
        if (clientOptions != null) {
            clientBuilder.options(clientOptions);
        }
        if (clientFactory != null) {
            clientBuilder.factory(clientFactory);
        }
        if (retry != null) {
            clientBuilder.decorator(retry);
        }
        if (logging != null) {
            clientBuilder.decorator(logging);
        }
        return clientBuilder.build();
    }

    private SessionProtocol protocol(String scheme) {
        final SessionProtocol protocol = SessionProtocol.of(scheme);
        switch (protocol) {
            case HTTP:
                if (protocolPreference == SessionProtocol.H1) {
                    // enforce HTTP/1 protocol
                    return SessionProtocol.H1C;
                }
                break;
            case HTTPS:
                if (protocolPreference == SessionProtocol.H1) {
                    // enforce HTTP/1 protocol
                    return SessionProtocol.H1;
                }
                break;
            default:
                break;
        }
        return protocol;
    }
}
