package com.github.scribejava.apis.facebook;

import com.github.scribejava.core.model.Response;
import java.io.IOException;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class FacebookAccessTokenJsonExtractorTest {

    private final FacebookAccessTokenJsonExtractor extractor = FacebookAccessTokenJsonExtractor.instance();

    @Test
    public void shouldThrowExceptionIfResponseIsError() throws IOException {
        final String body = "{\"error\":"
                + "{\"message\":\"This authorization code has been used.\","
                + "\"type\":\"OAuthException\","
                + "\"code\":100,"
                + "\"fbtrace_id\":\"DtxvtGRaxbB\"}}";
        try (Response response = error(body)) {

            final FacebookAccessTokenErrorResponse fateR = assertThrows(FacebookAccessTokenErrorResponse.class,
                    new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    extractor.extract(response);
                }
            });

            assertEquals("This authorization code has been used.", fateR.getMessage());
            assertEquals("OAuthException", fateR.getType());
            assertEquals(100, fateR.getCodeInt());
            assertEquals("DtxvtGRaxbB", fateR.getFbtraceId());
            assertEquals(body, fateR.getResponse().getBody());
        }
    }

    private static Response error(String body) {
        return new Response(400, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }
}
