package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.ObjectMother;
import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import static org.junit.Assert.assertThrows;
import org.junit.function.ThrowingRunnable;

public class BaseStringExtractorTest {

    private BaseStringExtractorImpl extractor;
    private OAuthRequest request;
    private OAuthRequest requestPort80;
    private OAuthRequest requestPort80v2;
    private OAuthRequest requestPort8080;
    private OAuthRequest requestPort443;
    private OAuthRequest requestPort443v2;

    @Before
    public void setUp() {
        request = ObjectMother.createSampleOAuthRequest();
        requestPort80 = ObjectMother.createSampleOAuthRequestPort80();
        requestPort80v2 = ObjectMother.createSampleOAuthRequestPort80v2();
        requestPort8080 = ObjectMother.createSampleOAuthRequestPort8080();
        requestPort443 = ObjectMother.createSampleOAuthRequestPort443();
        requestPort443v2 = ObjectMother.createSampleOAuthRequestPort443v2();
        extractor = new BaseStringExtractorImpl();
    }

    @Test
    public void shouldExtractBaseStringFromOAuthRequest() {
        final String expected = "GET&http%3A%2F%2Fexample.com&oauth_callback%3Dhttp%253A%252F%252Fexample%252Fcallback"
                + "%26oauth_consumer_key%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature%3DOAuth-Signature"
                + "%26oauth_timestamp%3D123456";
        final String baseString = extractor.extract(request);
        assertEquals(expected, baseString);
    }

    @Test
    public void shouldExcludePort80() {
        final String expected = "GET&http%3A%2F%2Fexample.com&oauth_callback%3Dhttp%253A%252F%252Fexample%252Fcallback"
                + "%26oauth_consumer_key%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature%3DOAuth-Signature"
                + "%26oauth_timestamp%3D123456";
        final String baseString = extractor.extract(requestPort80);
        assertEquals(expected, baseString);
    }

    @Test
    public void shouldExcludePort80v2() {
        final String expected = "GET&http%3A%2F%2Fexample.com%2Ftest&oauth_callback%3Dhttp%253A%252F%252Fexample"
                + "%252Fcallback%26oauth_consumer_key%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature"
                + "%3DOAuth-Signature%26oauth_timestamp%3D123456";
        final String baseString = extractor.extract(requestPort80v2);
        assertEquals(expected, baseString);
    }

    @Test
    public void shouldIncludePort8080() {
        final String expected = "GET&http%3A%2F%2Fexample.com%3A8080&oauth_callback%3Dhttp%253A%252F%252Fexample"
                + "%252Fcallback%26oauth_consumer_key%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature"
                + "%3DOAuth-Signature%26oauth_timestamp%3D123456";
        final String baseString = extractor.extract(requestPort8080);
        assertEquals(expected, baseString);
    }

    @Test
    public void shouldExcludePort443() {
        final String expected = "GET&https%3A%2F%2Fexample.com&oauth_callback%3Dhttp%253A%252F%252Fexample%252Fcallback"
                + "%26oauth_consumer_key%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature%3DOAuth-Signature"
                + "%26oauth_timestamp%3D123456";
        final String baseString = extractor.extract(requestPort443);
        assertEquals(expected, baseString);
    }

    @Test
    public void shouldExcludePort443v2() {
        final String expected = "GET&https%3A%2F%2Fexample.com%2Ftest&oauth_callback%3Dhttp%253A%252F%252Fexample"
                + "%252Fcallback%26oauth_consumer_key%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature"
                + "%3DOAuth-Signature%26oauth_timestamp%3D123456";
        final String baseString = extractor.extract(requestPort443v2);
        assertEquals(expected, baseString);
    }

    public void shouldThrowExceptionIfRquestIsNull() {
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                extractor.extract(null);
            }
        });
    }

    public void shouldThrowExceptionIfRquestHasNoOAuthParameters() {
        final OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertThrows(OAuthParametersMissingException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                extractor.extract(request);
            }
        });
    }

    @Test
    public void shouldProperlyEncodeSpaces() {
        final String expected = "GET&http%3A%2F%2Fexample.com&body%3Dthis%2520param%2520has%2520whitespace"
                + "%26oauth_callback%3Dhttp%253A%252F%252Fexample%252Fcallback%26oauth_consumer_key"
                + "%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature%3DOAuth-Signature%26oauth_timestamp%3D123456";
        request.addBodyParameter("body", "this param has whitespace");
        assertEquals(expected, extractor.extract(request));
    }
}
