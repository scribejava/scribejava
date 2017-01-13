package com.github.scribejava.apis.facebook;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

/**
 * non standard Facebook Extractor
 */
public class FacebookAccessTokenJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final String MESSAGE_REGEX = "\"message\"\\s*:\\s*\"([^\"]*?)\"";
    private static final String TYPE_REGEX = "\"type\"\\s*:\\s*\"([^\"]*?)\"";
    private static final String CODE_REGEX = "\"code\"\\s*:\\s*\"?([^\",}]*?)[\",}]";
    private static final String FBTRACE_ID_REGEX = "\"fbtrace_id\"\\s*:\\s*\"([^\"]*?)\"";

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
    protected void generateError(String response) {
        extractParameter(response, MESSAGE_REGEX, false);

        throw new FacebookAccessTokenErrorResponse(extractParameter(response, MESSAGE_REGEX, false),
                extractParameter(response, TYPE_REGEX, false), extractParameter(response, CODE_REGEX, false),
                extractParameter(response, FBTRACE_ID_REGEX, false), response);
    }

}
