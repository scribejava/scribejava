package com.github.scribejava.apis.facebook;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import java.io.IOException;
import java.util.Map;

/**
 * non standard Facebook Extractor
 */
public class FacebookAccessTokenJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    protected FacebookAccessTokenJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final FacebookAccessTokenJsonExtractor INSTANCE = new FacebookAccessTokenJsonExtractor();
    }

    public static FacebookAccessTokenJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * non standard. examples:<br>
     *
     * '{"error":{"message":"This authorization code has been
     * used.","type":"OAuthException","code":100,"fbtrace_id":"DtxvtGRaxbB"}}'<br>
     *
     * '{"error":{"message":"Error validating application. Invalid application
     * ID.","type":"OAuthException","code":101,"fbtrace_id":"CvDR+X4WWIx"}}'
     */
    @Override
    public void generateError(String rawResponse) throws IOException {
        @SuppressWarnings("unchecked")
        final Map<String, String> response = OAuth2AccessTokenJsonExtractor.OBJECT_MAPPER
                .readValue(rawResponse, Map.class);

        throw new FacebookAccessTokenErrorResponse(response.get("message"), response.get("type"), response.get("code"),
                response.get("fbtrace_id"), rawResponse);
    }

}
