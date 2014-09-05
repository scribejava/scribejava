package org.scribe.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.api.FacebookApi;
import org.scribe.exceptions.OAuthParametersMissingException;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.test.helpers.ObjectMother;

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
        try {
            assertEquals("OAuth oauth_callback=\"http%3A%2F%2Fexample%2Fcallback\", " + "oauth_signature=\"OAuth-Signature\", "
                    + "oauth_consumer_key=\"AS%23%24%5E%2A%40%26\", " + "oauth_timestamp=\"123456\"", header);
        } catch (AssertionError ae) {
            //maybe this is OpenJDK 8? Different order of elements in HashMap while iterating'em.
            assertEquals("OAuth " + "oauth_signature=\"OAuth-Signature\", " + "oauth_callback=\"http%3A%2F%2Fexample%2Fcallback\", "
                    + "oauth_consumer_key=\"AS%23%24%5E%2A%40%26\", " + "oauth_timestamp=\"123456\"", header);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldExceptionIfRequestIsNull() {
        final OAuthRequest nullRequest = null;
        extractor.extract(nullRequest);
    }

    @Test(expected = OAuthParametersMissingException.class)
    public void shouldExceptionIfRequestHasNoOAuthParams() {
        final OAuthRequest emptyRequest = new OAuthRequest(Verb.GET, "http://example.com", new OAuth20ServiceImpl(new FacebookApi(), new OAuthConfig(
                "test", "test")));
        extractor.extract(emptyRequest);
    }
}
