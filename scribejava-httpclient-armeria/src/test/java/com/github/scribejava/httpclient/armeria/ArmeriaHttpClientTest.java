package com.github.scribejava.httpclient.armeria;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;
import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.client.retry.Backoff;
import com.linecorp.armeria.client.retry.RetryRule;
import com.linecorp.armeria.client.retry.RetryingClient;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.logging.LogLevel;
import io.netty.channel.EventLoopGroup;
import io.netty.resolver.AbstractAddressResolver;
import io.netty.resolver.AddressResolver;
import io.netty.resolver.AddressResolverGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.slf4j.LoggerFactory;

public class ArmeriaHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        // simulate DNS resolution for a mock address ("kubernetes.docker.internal")
        final Function<? super EventLoopGroup, ? extends AddressResolverGroup<? extends InetSocketAddress>> addressRGF
                = eventLoopGroup -> new MockAddressResolverGroup();
        // No-Op DNS resolver to avoid resolution issues in the unit test
        final ClientFactory clientFactory = ClientFactory.builder().addressResolverGroupFactory(addressRGF).build();
        final ArmeriaHttpClientConfig config = new ArmeriaHttpClientConfig(null, clientFactory);

        // enable client-side HTTP tracing
        config.setLogging(LoggingClient.builder()
                .logger(LoggerFactory.getLogger("HTTP_TRACE"))
                .requestLogLevel(LogLevel.valueOf("INFO"))
                .successfulResponseLogLevel(LogLevel.valueOf("INFO"))
                .failureResponseLogLevel(LogLevel.valueOf("WARN"))
                .newDecorator());

        // enable request retry
        final Backoff retryBackoff = Backoff.of("exponential=200:10000,jitter=0.2,maxAttempts=5");
        final RetryRule retryRule = RetryRule.builder()
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .onUnprocessed()
                .thenBackoff(retryBackoff);

        return new ArmeriaHttpClient(config.withRetry(RetryingClient.newDecorator(retryRule)));
    }

    // No-Op DNS resolver to avoid resolution issues in the unit test
    private static class MockAddressResolverGroup extends AddressResolverGroup<InetSocketAddress> {

        @Override
        protected AddressResolver<InetSocketAddress> newResolver(EventExecutor executor) {
            return new MockAddressResolver(executor);
        }
    }

    private static class MockAddressResolver extends AbstractAddressResolver<InetSocketAddress> {

        private MockAddressResolver(EventExecutor executor) {
            super(executor);
        }

        @Override
        protected boolean doIsResolved(InetSocketAddress address) {
            return !address.isUnresolved();
        }

        private InetSocketAddress resolveToLoopback(InetSocketAddress unresolvedAddress) {
            return new InetSocketAddress(InetAddress.getLoopbackAddress(), unresolvedAddress.getPort());
        }

        @Override
        protected void doResolve(InetSocketAddress unresolvedAddress, Promise<InetSocketAddress> promise) {
            promise.setSuccess(resolveToLoopback(unresolvedAddress));
        }

        @Override
        protected void doResolveAll(InetSocketAddress unresolvedAddress, Promise<List<InetSocketAddress>> promise) {
            promise.setSuccess(Collections.singletonList(resolveToLoopback(unresolvedAddress)));
        }
    }
}
