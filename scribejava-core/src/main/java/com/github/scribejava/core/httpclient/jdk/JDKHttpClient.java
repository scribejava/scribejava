package com.github.scribejava.core.httpclient.jdk;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.httpclient.BodyPartPayload;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.MultipartPayload;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class JDKHttpClient implements HttpClient {

    private final JDKHttpClientConfig config;

    public JDKHttpClient() {
        this(JDKHttpClientConfig.defaultConfig());
    }

    public JDKHttpClient(JDKHttpClientConfig clientConfig) {
        config = clientConfig;
    }

    @Override
    public void close() {
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.BYTE_ARRAY, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.MULTIPART, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.STRING, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {
        throw new UnsupportedOperationException("JDKHttpClient does not support File payload for the moment");
    }

    private <T> Future<T> doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, BodyType bodyType, Object bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        try {
            final Response response = doExecute(userAgent, headers, httpVerb, completeUrl, bodyType, bodyContents);
            @SuppressWarnings("unchecked")
            final T t = converter == null ? (T) response : converter.convert(response);
            if (callback != null) {
                callback.onCompleted(t);
            }
            return new JDKHttpFuture<>(t);
        } catch (IOException | RuntimeException e) {
            if (callback != null) {
                callback.onThrowable(e);
            }
            return new JDKHttpFuture<>(e);
        }
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents) throws InterruptedException, ExecutionException, IOException {
        return doExecute(userAgent, headers, httpVerb, completeUrl, BodyType.BYTE_ARRAY, bodyContents);
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload multipartPayloads) throws InterruptedException, ExecutionException, IOException {
        return doExecute(userAgent, headers, httpVerb, completeUrl, BodyType.MULTIPART, multipartPayloads);
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents) throws InterruptedException, ExecutionException, IOException {
        return doExecute(userAgent, headers, httpVerb, completeUrl, BodyType.STRING, bodyContents);
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents) throws InterruptedException, ExecutionException, IOException {
        throw new UnsupportedOperationException("JDKHttpClient does not support File payload for the moment");
    }

    private Response doExecute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            BodyType bodyType, Object bodyContents) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(completeUrl).openConnection();
        connection.setInstanceFollowRedirects(config.isFollowRedirects());
        connection.setRequestMethod(httpVerb.name());
        if (config.getConnectTimeout() != null) {
            connection.setConnectTimeout(config.getConnectTimeout());
        }
        if (config.getReadTimeout() != null) {
            connection.setReadTimeout(config.getReadTimeout());
        }
        addHeaders(connection, headers, userAgent);
        if (httpVerb.isPermitBody()) {
            bodyType.setBody(connection, bodyContents, httpVerb.isRequiresBody());
        }

        try {
            connection.connect();
            final int responseCode = connection.getResponseCode();
            return new Response(responseCode, connection.getResponseMessage(), parseHeaders(connection),
                    responseCode >= 200 && responseCode < 400 ? connection.getInputStream()
                            : connection.getErrorStream());
        } catch (UnknownHostException e) {
            throw new OAuthException("The IP address of a host could not be determined.", e);
        }
    }

    private enum BodyType {
        BYTE_ARRAY {
            @Override
            void setBody(HttpURLConnection connection, Object bodyContents, boolean requiresBody) throws IOException {
                addBody(connection, (byte[]) bodyContents, requiresBody);
            }
        },
        MULTIPART {
            @Override
            void setBody(HttpURLConnection connection, Object bodyContents, boolean requiresBody) throws IOException {
                addBody(connection, (MultipartPayload) bodyContents, requiresBody);
            }
        },
        STRING {
            @Override
            void setBody(HttpURLConnection connection, Object bodyContents, boolean requiresBody) throws IOException {
                addBody(connection, ((String) bodyContents).getBytes(), requiresBody);
            }
        };

        abstract void setBody(HttpURLConnection connection, Object bodyContents, boolean requiresBody)
                throws IOException;
    }

    private static Map<String, String> parseHeaders(HttpURLConnection conn) {
        final Map<String, String> headers = new HashMap<>();

        for (Map.Entry<String, List<String>> headerField : conn.getHeaderFields().entrySet()) {
            final String key = headerField.getKey();
            final String value = headerField.getValue().get(0);
            if ("Content-Encoding".equalsIgnoreCase(key)) {
                headers.put("Content-Encoding", value);
            } else {
                headers.put(key, value);
            }
        }
        return headers;
    }

    private static void addHeaders(HttpURLConnection connection, Map<String, String> headers, String userAgent) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        if (userAgent != null) {
            connection.setRequestProperty(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }
    }

    /**
     * Multipart implementation supporting more than one payload
     */
    private static void addBody(HttpURLConnection connection, MultipartPayload multipartPayload, boolean requiresBody)
            throws IOException {

        final int contentLength = multipartPayload.getContentLength();
        if (requiresBody || contentLength > 0) {
            final OutputStream os = prepareConnectionForBodyAndGetOutputStream(connection, contentLength);

            for (BodyPartPayload bodyPart : multipartPayload.getBodyParts()) {
                os.write(multipartPayload.getStartBoundary(bodyPart));
                os.write(bodyPart.getPayload());
                os.write(multipartPayload.getEndBoundary());
            }
        }
    }

    private static void addBody(HttpURLConnection connection, byte[] content, boolean requiresBody) throws IOException {
        final int contentLength = content.length;
        if (requiresBody || contentLength > 0) {
            prepareConnectionForBodyAndGetOutputStream(connection, contentLength).write(content);
        }
    }

    private static OutputStream prepareConnectionForBodyAndGetOutputStream(HttpURLConnection connection,
            int contentLength) throws IOException {

        connection.setRequestProperty(CONTENT_LENGTH, String.valueOf(contentLength));
        if (connection.getRequestProperty(CONTENT_TYPE) == null) {
            connection.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);

        }
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }

}
