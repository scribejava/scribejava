package com.github.scribejava.apis.facebook;

import com.github.scribejava.core.model.Response;
import java.io.IOException;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

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
            extractor.extract(response);
            fail();
        } catch (FacebookAccessTokenErrorResponse fateR) {
            assertEquals("This authorization code has been used.", fateR.getMessage());
            assertEquals("OAuthException", fateR.getType());
            assertEquals(100, fateR.getCodeInt());
            assertEquals("DtxvtGRaxbB", fateR.getFbtraceId());
            assertEquals(body, fateR.getRawResponse());
        }
    }

    private static Response error(String body) {
        return new Response(400, /* message */ null, /* headers */ Collections.<String, String>emptyMap(), body);
    }
}
