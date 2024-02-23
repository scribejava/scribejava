package com.github.scribejava.httpclient.okhttp;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OkHttpDecoratorTest {

    @Test
    public void testHttpClient() throws Exception {

        final UUID trackId = UUID.randomUUID();

        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new TrackInterceptor(trackId))
                .build();

        final OkHttpHttpClient client = new OkHttpHttpClient(httpClient, new TrackDecorator(trackId));

        final Response response = client.execute("", Collections.<String, String>emptyMap(),
                Verb.GET, "http://dummy/", (byte[]) null);

        assertNotNull(response);
    }

    @Test
    public void testHttpConfig() throws Exception {

        final UUID trackId = UUID.randomUUID();

        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new TrackInterceptor(trackId));

        final OkHttpHttpClientConfig config = new OkHttpHttpClientConfig(builder)
                .withDecorator(new TrackDecorator(trackId));

        final OkHttpHttpClient client = new OkHttpHttpClient(config);

        final Response response = client.execute("", Collections.<String, String>emptyMap(),
                Verb.GET, "http://dummy/", (byte[]) null);

        assertNotNull(response);
    }

    public static class TrackInterceptor implements Interceptor {

        private final UUID trackId;

        public TrackInterceptor(UUID trackId) {
            this.trackId = trackId;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            final okhttp3.Request request = chain.request();

            final UUID value = request.tag(UUID.class);

            assertNotNull(value);
            assertEquals(trackId, value);

            return new okhttp3.Response.Builder()
                    .code(200)
                    .message("OK")
                    .body(ResponseBody.create("Nothing here", MediaType.parse("text/plain")))
                    .protocol(Protocol.HTTP_1_1)
                    .request(request).build();
        }
    }

    public static class TrackDecorator implements OkHttpRequestDecorator {

        private final UUID uuid;

        public TrackDecorator(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void decorate(Builder requestBuilder) {
            requestBuilder.tag(UUID.class, uuid);
        }
    }
}
