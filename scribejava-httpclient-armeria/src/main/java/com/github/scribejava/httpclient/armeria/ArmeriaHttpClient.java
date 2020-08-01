package com.github.scribejava.httpclient.armeria;

import static java.util.Objects.requireNonNull;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.httpclient.multipart.MultipartPayload;
import com.github.scribejava.core.httpclient.multipart.MultipartUtils;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.common.RequestHeadersBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * An implementation of {@link AbstractAsyncOnlyHttpClient} based on
 * <a href="https://line.github.io/armeria/">Armeria HTTP client</a>.
 */
public class ArmeriaHttpClient extends AbstractAsyncOnlyHttpClient {

    /**
     * A builder of new instances of Armeria's {@link WebClient}
     */
    private final ArmeriaWebClientBuilder clientBuilder;
    /**
     * A list of cached Endpoints. It helps avoiding building a new Endpoint per each request.
     */
    private final Map<String, WebClient> httpClients = new HashMap<>();
    /**
     * A read/write lock to access the list of cached Endpoints concurrently.
     */
    private final ReentrantReadWriteLock httpClientsLock = new ReentrantReadWriteLock();

    public ArmeriaHttpClient() {
        this(ArmeriaHttpClientConfig.defaultConfig());
    }

    public ArmeriaHttpClient(ArmeriaHttpClientConfig config) {
        this.clientBuilder = config.builder();
    }

    /**
     * Cleans up the list of cached Endpoints.
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
        final WebClient client = getClient(uri);

        // Build HTTP request
        final RequestHeadersBuilder headersBuilder
                = RequestHeaders.of(getHttpMethod(httpVerb), path).toBuilder();
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
     * Provides an instance of {@link WebClient} for a given endpoint {@link URI} based on an endpoint as
     * {@code scheme://authority}.
     *
     * @param uri an endpoint {@link URI}
     * @return {@link WebClient} instance
     */
    private WebClient getClient(final URI uri) {
        final String endpoint = getEndPoint(uri);

        WebClient client;
        final Lock readLock = httpClientsLock.readLock();
        readLock.lock();
        try {
            client = httpClients.get(endpoint);
        } finally {
            readLock.unlock();
        }

        if (client != null) {
            return client;
        }

        client = clientBuilder.newWebClient(
                requireNonNull(uri.getScheme(), "scheme"),
                requireNonNull(uri.getAuthority(), "authority"));

        final Lock writeLock = httpClientsLock.writeLock();
        writeLock.lock();
        try {
            if (!httpClients.containsKey(endpoint)) {
                httpClients.put(endpoint, client);
                return client;
            } else {
                return httpClients.get(endpoint);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Extracts {@code scheme} and {@code authority} portion of the {@link URI}. Assuming the {@link URI} as the
     * following: {@code URI = scheme:[//authority]path[?query][#fragment]}
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    private static String getEndPoint(final URI uri) {
        final StringBuilder builder = new StringBuilder()
                .append(requireNonNull(uri.getScheme(), "scheme"))
                .append("://").append(requireNonNull(uri.getAuthority(), "authority"));
        return builder.toString();
    }

    /**
     * Extracts {@code path}, {@code query) and {@code fragment} portion of the {@link URI}.
     * Assuming the {@link URI} as the following:
     * {@code URI = scheme:[//authority]path[?query][#fragment]}
     */
    private static String getServicePath(final URI uri) {
        final StringBuilder builder = new StringBuilder()
                .append(requireNonNull(uri.getPath(), "path"));
        final String query = uri.getQuery();
        if (query != null) {
            builder.append('?').append(query);
        }
        final String fragment = uri.getFragment();
        if (fragment != null) {
            builder.append('#').append(fragment);
        }
        return builder.toString();
    }

    /**
     * Maps {@link Verb} to {@link HttpMethod}
     *
     * @param httpVerb a {@link Verb} to match with {@link HttpMethod}
     * @return {@link HttpMethod} corresponding to the parameter
     */
    private static HttpMethod getHttpMethod(final Verb httpVerb) {
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
     *
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
     * Converts {@link AggregatedHttpResponse} to {@link Response} upon its aggregation completion and invokes
     * {@link OAuthAsyncRequestCallback} for it.
     *
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
            @SuppressWarnings("unchecked")
            final T t
                    = converter == null ? (T) response : converter.convert(response);
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
     *
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
