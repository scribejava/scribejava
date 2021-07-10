package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.ObjectMother;
import org.junit.function.ThrowingRunnable;

public class HeaderExtractorTest {

    private HeaderExtractorImpl extractor;
    private OAuthRequest request;

    @Before
    public void setUp() {
        request = ObjectMother.createSampleOAuthRequest();
        extractor = new HeaderExtractorImpl();
    }

    @Test
    public void shouldExtractStandardHeader() {
        final String header = extractor.extract(request);
        final String oauth = "OAuth ";
        final String callback = "oauth_callback=\"http%3A%2F%2Fexample%2Fcallback\"";
        final String signature = "oauth_signature=\"OAuth-Signature\"";
        final String key = "oauth_consumer_key=\"AS%23%24%5E%2A%40%26\"";
        final String timestamp = "oauth_timestamp=\"123456\"";

        assertTrue(header.startsWith(oauth));
        assertTrue(header.contains(callback));
        assertTrue(header.contains(signature));
        assertTrue(header.contains(key));
        assertTrue(header.contains(timestamp));
        // Assert that header only contains the checked elements above and nothing else
        assertEquals(", , , ",
                header.replaceFirst(oauth, "")
                        .replaceFirst(callback, "")
                        .replaceFirst(signature, "")
                        .replaceFirst(key, "")
                        .replaceFirst(timestamp, ""));
    }

    public void shouldExceptionIfRequestIsNull() {
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                extractor.extract(null);
            }
        });
    }

    public void shouldExceptionIfRequestHasNoOAuthParams() {
        final OAuthRequest emptyRequest = new OAuthRequest(Verb.GET, "http://example.com");
        assertThrows(OAuthParametersMissingException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                extractor.extract(emptyRequest);
            }
        });
    }
}
