package com.github.scribejava.apis.facebook;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import java.util.regex.Pattern;

/**
 * non standard Facebook Extractor
 */
public class FacebookAccessTokenJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern MESSAGE_REGEX_PATTERN = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]*?)\"");
    private static final Pattern TYPE_REGEX_PATTERN = Pattern.compile("\"type\"\\s*:\\s*\"([^\"]*?)\"");
    private static final Pattern CODE_REGEX_PATTERN = Pattern.compile("\"code\"\\s*:\\s*\"?([^\",}]*?)[\",}]");
    private static final Pattern FBTRACE_ID_REGEX_PATTERN = Pattern.compile("\"fbtrace_id\"\\s*:\\s*\"([^\"]*?)\"");

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
    public void generateError(String response) {
        extractParameter(response, MESSAGE_REGEX_PATTERN, false);

        throw new FacebookAccessTokenErrorResponse(extractParameter(response, MESSAGE_REGEX_PATTERN, false),
                extractParameter(response, TYPE_REGEX_PATTERN, false),
                extractParameter(response, CODE_REGEX_PATTERN, false),
                extractParameter(response, FBTRACE_ID_REGEX_PATTERN, false), response);
    }

}
