package com.github.scribejava.httpclient.armeria;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;
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

public class ArmeriaHttpClientTest extends AbstractClientTest {

  @Override
  protected HttpClient createNewClient() {
    final ArmeriaHttpClientConfig config = ArmeriaHttpClientConfig.defaultConfig();
    // simulate DNS resolution for a mock address ("kubernetes.docker.internal")
    final Function<? super EventLoopGroup, ? extends AddressResolverGroup<? extends InetSocketAddress>>
        addressResolverGroupFactory = eventLoopGroup -> new MockAddressResolverGroup();
    config.setFactoryOptions(// No-Op DNS resolver to avoid resolution issues in the unit test
        Collections.singletonMap("ADDRESS_RESOLVER_GROUP_FACTORY", addressResolverGroupFactory));
    // enable client-side HTTP tracing
    config.setLogging(true);
    return new ArmeriaHttpClient(config);
  }

  //------------------------------------------------------------------------------------------------
  // No-Op DNS resolver to avoid resolution issues in the unit test

  static class MockAddressResolverGroup extends AddressResolverGroup<InetSocketAddress> {

    private MockAddressResolverGroup() {
    }

    protected AddressResolver<InetSocketAddress> newResolver(EventExecutor executor) {
      return new MockAddressResolver(executor);
    }
  }

  static class MockAddressResolver extends AbstractAddressResolver<InetSocketAddress> {

    private MockAddressResolver(EventExecutor executor) {
      super(executor);
    }

    protected boolean doIsResolved(InetSocketAddress address) {
      return !address.isUnresolved();
    }

    private InetSocketAddress resolveToLoopback(InetSocketAddress unresolvedAddress) {
      return new InetSocketAddress(InetAddress.getLoopbackAddress(), unresolvedAddress.getPort());
    }

    protected void doResolve(InetSocketAddress unresolvedAddress, Promise<InetSocketAddress> promise) {
      promise.setSuccess(resolveToLoopback(unresolvedAddress));
    }

    protected void doResolveAll(InetSocketAddress unresolvedAddress, Promise<List<InetSocketAddress>> promise) {
      promise.setSuccess(Collections.singletonList(resolveToLoopback(unresolvedAddress)));
    }
  }

  //------------------------------------------------------------------------------------------------
}
