package com.github.scribejava.apis.fitbit;

import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse.ErrorCode;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;

public class FitBitJsonTokenExtractorTest {

    private static final String ERROR_DESCRIPTION = "Authorization code invalid: " +
            "cbb1c11b23209011e89be71201fa6381464dc0af " +
            "Visit https://dev.fitbit.com/docs/oauth2 for more information " +
            "on the Fitbit Web API authorization process.";
    private static final String ERROR_JSON = "{\"errors\":[{\"errorType\":\"invalid_grant\",\"message\":\"" +
            ERROR_DESCRIPTION + "\"}],\"success\":false}";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testErrorExtraction() {

        final FitBitJsonTokenExtractor extractor = new FitBitJsonTokenExtractor();

        thrown.expect(OAuth2AccessTokenErrorResponse.class);
        thrown.expect(errorCode(ErrorCode.invalid_grant));
        thrown.expect(errorDescription(ERROR_DESCRIPTION));

        extractor.generateError(ERROR_JSON);

    }

    private Matcher<OAuth2AccessTokenErrorResponse> errorCode(ErrorCode expected) {
        return new FeatureMatcher<OAuth2AccessTokenErrorResponse, ErrorCode>(
                equalTo(expected),
                "a response with errorCode",
                "errorCode") {
            @Override
            protected ErrorCode featureValueOf(OAuth2AccessTokenErrorResponse actual) {
                return actual.getErrorCode();
            }
        };
    }

    private Matcher<OAuth2AccessTokenErrorResponse> errorDescription(String expected) {
        return new FeatureMatcher<OAuth2AccessTokenErrorResponse, String>(
                equalTo(expected),
                "a response with errorDescription",
                "errorDescription") {
            @Override
            protected String featureValueOf(OAuth2AccessTokenErrorResponse actual) {
                return actual.getErrorDescription();
            }
        };
    }
}
