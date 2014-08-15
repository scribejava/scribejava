package org.scribe.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthParametersMissingException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.test.helpers.ObjectMother;

public class HeaderExtractorTest {

    private HeaderExtractorImpl extractor;
    private OAuthRequest request;

    @Before
    public void setup() {
        request = ObjectMother.createSampleOAuthRequest();
        extractor = new HeaderExtractorImpl();
    }

    @Test
    public void shouldExtractStandardHeader() {
        String expected = "OAuth oauth_callback=\"http%3A%2F%2Fexample%2Fcallback\", " + "oauth_signature=\"OAuth-Signature\", "
                + "oauth_consumer_key=\"AS%23%24%5E%2A%40%26\", " + "oauth_timestamp=\"123456\"";
        String header = extractor.extract(request);
        assertEquals(expected, header);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldExceptionIfRequestIsNull() {
        OAuthRequest nullRequest = null;
        extractor.extract(nullRequest);
    }

    @Test(expected = OAuthParametersMissingException.class)
    public void shouldExceptionIfRequestHasNoOAuthParams() {
        OAuthRequest emptyRequest = new OAuthRequest(Verb.GET, "http://example.com");
        extractor.extract(emptyRequest);
    }
}
