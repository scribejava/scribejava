package com.github.scribejava.httpclient.armeria;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.httpclient.multipart.MultipartPayload;
import com.github.scribejava.core.httpclient.multipart.MultipartUtils;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.ClientFactoryBuilder;
import com.linecorp.armeria.client.ClientFactoryOptionValue;
import com.linecorp.armeria.client.ClientOptions;
import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.WebClientBuilder;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.common.RequestHeadersBuilder;
import com.linecorp.armeria.common.SessionProtocol;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class ArmeriaHttpClient extends AbstractAsyncOnlyHttpClient {

  private final ArmeriaHttpClientConfig config;
  private final Map<String, WebClient> httpClients = new HashMap<>();
  private final ReentrantReadWriteLock httpClientsLock = new ReentrantReadWriteLock();

  public ArmeriaHttpClient() {
    this(ArmeriaHttpClientConfig.defaultConfig());
  }

  public ArmeriaHttpClient(ArmeriaHttpClientConfig config) {
    this.config = config;
  }

  public HttpClientConfig getConfig() {
    return config;
  }

  /**
   * Cleans up HTTP clients collection.
   */
  @Override
  public void close() {
    final Lock writeLock = httpClientsLock.writeLock();
    writeLock.lock();
    try {
      httpClients.clear();
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
      String completeUrl, byte[] bodyContents, OAuthAsyncRequestCallback<T> callback,
      OAuthRequest.ResponseConverter<T> converter) {
    return doExecuteAsync(userAgent, headers, httpVerb, completeUrl,
        new BytesBody(bodyContents), callback, converter);
  }

  @Override
  public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
      String completeUrl, MultipartPayload bodyContents, OAuthAsyncRequestCallback<T> callback,
      OAuthRequest.ResponseConverter<T> converter) {
    return doExecuteAsync(userAgent, headers, httpVerb, completeUrl,
        new MultipartBody(bodyContents), callback, converter);
  }

  @Override
  public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
      String completeUrl, String bodyContents, OAuthAsyncRequestCallback<T> callback,
      OAuthRequest.ResponseConverter<T> converter) {
    return doExecuteAsync(userAgent, headers, httpVerb, completeUrl,
        new StringBody(bodyContents), callback, converter);
  }

  @Override
  public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
      String completeUrl, File bodyContents, OAuthAsyncRequestCallback<T> callback,
      OAuthRequest.ResponseConverter<T> converter) {
    return doExecuteAsync(userAgent, headers, httpVerb, completeUrl,
        new FileBody(bodyContents), callback, converter);
  }

  private <T> CompletableFuture<T> doExecuteAsync(String userAgent,
      Map<String, String> headers, Verb httpVerb,
      String completeUrl, Supplier<HttpData> contentSupplier,
      OAuthAsyncRequestCallback<T> callback,
      OAuthRequest.ResponseConverter<T> converter) {
    // Get the URI and Path
    final URI uri = URI.create(completeUrl);
    final String path = getServicePath(uri);

    // Fetch/Create WebClient instance for a given Endpoint
    final WebClient client = getHttpClient(uri);

    // Build HTTP request
    final RequestHeadersBuilder headersBuilder =
        RequestHeaders.of(getHttpMethod(httpVerb), path).toBuilder();
    headersBuilder.add(headers.entrySet());
    if (userAgent != null) {
      headersBuilder.add(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
    }

    // Build the request body and execute HTTP request
    final HttpResponse response;
    if (httpVerb.isPermitBody()) { // POST, PUT, PATCH and DELETE methods
      final HttpData contents = contentSupplier.get();
      if (httpVerb.isRequiresBody() && contents == null) { // POST or PUT methods
        throw new IllegalArgumentException(
            "Contents missing for request method " + httpVerb.name());
      }
      if (contents != null) {
        response = client.execute(headersBuilder.build(), contents);
      } else {
        response = client.execute(headersBuilder.build());
      }
    } else {
      response = client.execute(headersBuilder.build());
    }

    // Aggregate HTTP response (asynchronously) and return the result Future
    return response.aggregate()
        .thenApply(r -> whenResponseComplete(callback, converter, r))
        .exceptionally(e -> completeExceptionally(callback, e));
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Extracts Protocol + Host + Port part from a given endpoint {@link URI}
   * @param uri an endpoint {@link URI}
   * @return a portion of {@link URI} including a combination of Protocol + Host + Port
   * @see #getHttpClient(URI uri)
   */
  private String getHostUri(final URI uri) {
    final StringBuilder builder = new StringBuilder();
    builder.append(uri.getScheme()).append("://").append(uri.getHost());
    final int port = uri.getPort();
    if (port > 0) {
      builder.append(":").append(port);
    }
    return builder.toString();
  }

  /**
   * Extracts service path from a given endpoint {@link URI}
   * @param uri an endpoint {@link URI}
   * @return service path portion of the {@link URI}
   */
  private static String getServicePath(final URI uri) {
    final StringBuilder builder = new StringBuilder();
    final String path = uri.getRawPath();
    if (path != null && path.length() > 0) {
      builder.append(path);
    } else {
      builder.append("/");
    }
    final String query = uri.getRawQuery();
    if (query != null && query.length() > 0) {
      builder.append("?").append(query);
    }
    return builder.toString();
  }

  /**
   * Provides and instance of {@link WebClient} for a given endpoint {@link URI} based on
   * a combination of Protocol + Host + Port.
   * @param uri an endpoint {@link URI}
   * @return {@link WebClient} instance
   */
  private WebClient getHttpClient(final URI uri) {
    final String hostUri = getHostUri(uri);

    WebClient client;
    final Lock readLock = httpClientsLock.readLock();
    readLock.lock();
    try {
      client = httpClients.get(hostUri);
    } finally {
      readLock.unlock();
    }

    if (client != null) {
      return client;
    }

    client = buildNewHttpClient(uri);

    final Lock writeLock = httpClientsLock.writeLock();
    writeLock.lock();
    try {
      if (!httpClients.containsKey(hostUri)) {
        httpClients.put(hostUri, client);
        return client;
      } else {
        return httpClients.get(hostUri);
      }
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Builds a brand-new instance of {@link WebClient} for a given endpoint {@link URI}.
   * Uses {@link ArmeriaHttpClientConfig} to configure the builder and the client.
   * @param uri an endpoint {@link URI}
   * @return Brand-new {@link WebClient} instance
   */
  private WebClient buildNewHttpClient(final URI uri) {
    // Build the client and the headers
    WebClientBuilder clientBuilder =
        getHttpClientBuilder(uri, config.getSessionProtocolPreference());

    final ClientOptions clientOptions = config.getClientOptions();
    if (clientOptions != null) {
      clientBuilder.options(clientOptions);
    }

    final ClientFactoryBuilder clientFactoryBuilder = ClientFactory.builder();
    final Collection<ClientFactoryOptionValue<?>> clientFactoryOptions = config.getClientFactoryOptions();
    if (clientFactoryOptions != null && !clientFactoryOptions.isEmpty()) {
      clientFactoryOptions.forEach(clientFactoryBuilder::option);
    }
    clientBuilder.factory(clientFactoryBuilder.build());

    if (config.isLogging()) {
      clientBuilder = clientBuilder.decorator(LoggingClient.newDecorator());
    }

    return clientBuilder.build();
  }

  /**
   * Provides an instance of {@link WebClientBuilder} for a given endpoint {@link URI}
   * @param uri an endpoint {@link URI}
   * @param protocolPreference an HTTP protocol generation preference; acceptable values: "h1" or "h2".
   * @return {@link WebClientBuilder} instance to build a new {@link WebClient}
   */
  private WebClientBuilder getHttpClientBuilder(final URI uri, final SessionProtocol protocolPreference) {
    final SessionProtocol sessionProtocol = SessionProtocol.of(uri.getScheme());
    final String host = uri.getHost();
    final int port = uri.getPort();
    final Endpoint endpoint = (port > 0) ? Endpoint.of(host, port) : Endpoint.of(host);
    switch(sessionProtocol) {
      case HTTP:
        if (protocolPreference == SessionProtocol.H1) {
          // enforce HTTP/1 protocol
          return WebClient.builder(SessionProtocol.H1C, endpoint);
        }
        break;
      case HTTPS:
        if (protocolPreference == SessionProtocol.H1) {
          // enforce HTTP/1 protocol
          return WebClient.builder(SessionProtocol.H1, endpoint);
        }
        break;
      default:
        break;
    }
    return WebClient.builder(sessionProtocol, endpoint);
  }

  /**
   * Maps {@link Verb} to {@link HttpMethod}
   * @param httpVerb a {@link Verb} to match with {@link HttpMethod}
   * @return {@link HttpMethod} corresponding to the parameter
   */
  private HttpMethod getHttpMethod(final Verb httpVerb) {
    switch (httpVerb) {
      case GET:
        return HttpMethod.GET;
      case POST:
        return HttpMethod.POST;
      case PUT:
        return HttpMethod.PUT;
      case DELETE:
        return HttpMethod.DELETE;
      case HEAD:
        return HttpMethod.HEAD;
      case OPTIONS:
        return HttpMethod.OPTIONS;
      case TRACE:
        return HttpMethod.TRACE;
      case PATCH:
        return HttpMethod.PATCH;
      default:
        throw new IllegalArgumentException(
            "message build error: unsupported HTTP method: " + httpVerb.name());
    }
  }

  //------------------------------------------------------------------------------------------------
  // Response asynchronous handlers

  /**
   * Converts {@link AggregatedHttpResponse} to {@link Response}
   * @param aggregatedResponse an instance of {@link AggregatedHttpResponse} to convert to {@link Response}
   * @return a {@link Response} converted from {@link AggregatedHttpResponse}
   */
  private Response convertResponse(final AggregatedHttpResponse aggregatedResponse) {
    final Map<String, String> headersMap = new HashMap<>(aggregatedResponse.headers().size());
    aggregatedResponse.headers()
        .forEach((header, value) -> headersMap.put(header.toString(), value));
    return new Response(aggregatedResponse.status().code(),
        aggregatedResponse.status().reasonPhrase(),
        headersMap, aggregatedResponse.content().toInputStream());
  }

  /**
   * Converts {@link AggregatedHttpResponse} to {@link Response} upon its aggregation completion
   * and invokes {@link OAuthAsyncRequestCallback} for it.
   * @param callback a {@link OAuthAsyncRequestCallback} callback to invoke upon response completion
   * @param converter an optional {@link OAuthRequest.ResponseConverter} result converter for {@link Response}
   * @param aggregatedResponse a source {@link AggregatedHttpResponse} to handle
   * @param <T> converter {@link OAuthRequest.ResponseConverter} specific type or {@link Response}
   * @return either instance of {@link Response} or converted result based on {@link OAuthRequest.ResponseConverter}
   */
  private <T> T whenResponseComplete(OAuthAsyncRequestCallback<T> callback,
      OAuthRequest.ResponseConverter<T> converter, AggregatedHttpResponse aggregatedResponse) {
    final Response response = convertResponse(aggregatedResponse);
    try {
      @SuppressWarnings("unchecked") final T t =
          converter == null ? (T) response : converter.convert(response);
      if (callback != null) {
        callback.onCompleted(t);
      }
      return t;
    } catch (IOException | RuntimeException e) {
      return completeExceptionally(callback, e);
    }
  }

  /**
   * Invokes {@link OAuthAsyncRequestCallback} upon {@link Throwable} error result
   * @param callback a {@link OAuthAsyncRequestCallback} callback to invoke upon response completion
   * @param throwable a {@link Throwable} error result
   * @param <T> converter {@link OAuthRequest.ResponseConverter} specific type or {@link Response}
   * @return null
   */
  private <T> T completeExceptionally(OAuthAsyncRequestCallback<T> callback, Throwable throwable) {
    if (callback != null) {
      callback.onThrowable(throwable);
    }
    return null;
  }

  //------------------------------------------------------------------------------------------------
  // Body type suppliers

  private static class BytesBody implements Supplier<HttpData> {

    private final byte[] bodyContents;

    BytesBody(final byte[] bodyContents) {
      this.bodyContents = bodyContents;
    }

    @Override
    public HttpData get() {
      return (bodyContents != null) ? HttpData.wrap(bodyContents) : null;
    }
  }

  private static class StringBody implements Supplier<HttpData> {

    private final String bodyContents;

    StringBody(final String bodyContents) {
      this.bodyContents = bodyContents;
    }

    @Override
    public HttpData get() {
      return (bodyContents != null) ? HttpData.ofUtf8(bodyContents) : null;
    }
  }

  private static class FileBody implements Supplier<HttpData> {

    private final File bodyContents;

    FileBody(final File bodyContents) {
      this.bodyContents = bodyContents;
    }

    @Override
    public HttpData get() {
      try {
        return (bodyContents != null)
            ? HttpData.wrap(Files.readAllBytes(bodyContents.toPath()))
            : null;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static class MultipartBody implements Supplier<HttpData> {

    private final MultipartPayload bodyContents;

    MultipartBody(final MultipartPayload bodyContents) {
      this.bodyContents = bodyContents;
    }

    @Override
    public HttpData get() {
      try {
        return (bodyContents != null)
            ? HttpData.wrap(MultipartUtils.getPayload(bodyContents).toByteArray())
            : null;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  //------------------------------------------------------------------------------------------------
}
