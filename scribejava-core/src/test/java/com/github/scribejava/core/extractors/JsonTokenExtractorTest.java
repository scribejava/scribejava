package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.github.scribejava.core.model.Token;

public class JsonTokenExtractorTest {

    private static final String RESPONSE = "'{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\"}'";
    private final JsonTokenExtractor extractor = new JsonTokenExtractor();

    @Test
    public void shouldParseResponse() {
        final Token token = extractor.extract(RESPONSE);
        assertEquals(token.getToken(), "I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForNullParameters() {
        extractor.extract(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForEmptyStrings() {
        extractor.extract("");
    }
}
