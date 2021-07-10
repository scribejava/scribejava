package com.github.scribejava.apis.fitbit;

import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth2.OAuth2Error;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import org.junit.function.ThrowingRunnable;

public class FitBitJsonTokenExtractorTest {

    private static final String ERROR_DESCRIPTION = "Authorization code invalid: "
            + "cbb1c11b23209011e89be71201fa6381464dc0af "
            + "Visit https://dev.fitbit.com/docs/oauth2 for more information "
            + "on the Fitbit Web API authorization process.";
    private static final String ERROR_JSON = "{\"errors\":[{\"errorType\":\"invalid_grant\",\"message\":\""
            + ERROR_DESCRIPTION + "\"}],\"success\":false}";

    @Test
    public void testErrorExtraction() throws IOException {

        final FitBitJsonTokenExtractor extractor = new FitBitJsonTokenExtractor();
        final OAuth2AccessTokenErrorResponse thrown = assertThrows(OAuth2AccessTokenErrorResponse.class,
                new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                extractor.generateError(new Response(403, null, null, ERROR_JSON));
            }
        });
        assertSame(OAuth2Error.INVALID_GRANT, thrown.getError());
        assertEquals(ERROR_DESCRIPTION, thrown.getErrorDescription());
    }
}
