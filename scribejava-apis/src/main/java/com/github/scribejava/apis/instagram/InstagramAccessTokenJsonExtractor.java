package com.github.scribejava.apis.instagram;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.apis.facebook.FacebookAccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.Response;

/**
 * non standard Facebook Extractor
 */
public class InstagramAccessTokenJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    protected InstagramAccessTokenJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final InstagramAccessTokenJsonExtractor INSTANCE = new InstagramAccessTokenJsonExtractor();
    }

    public static InstagramAccessTokenJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Non standard error message. Could be Instagram or Facebook specific.
     * Usually Instagram type is used for getting access tokens. Facebook type is used for
     * refreshing tokens.
     *
     * examples:<br>
     *
     * Instagram specific:
     * '{"error_type": "OAuthException", "code": 400, "error_message": "Missing required field client_id"}'
     *
     * Facebook specific:
     * '{"error":{"message":"Error validating application. Invalid application
     * ID.","type":"OAuthException","code":101,"fbtrace_id":"CvDR+X4WWIx"}}'
     *
     * @param response response
     */
    @Override
    public void generateError(Response response) throws IOException {
        final JsonNode errorNode = OAuth2AccessTokenJsonExtractor.OBJECT_MAPPER
                .readTree(response.getBody());
        JsonNode error = errorNode.get("error");
        if (error != null) {
          FacebookAccessTokenJsonExtractor.instance().generateError(response);
        } else {
          throw new InstagramAccessTokenErrorResponse(
              errorNode.get("error_type").asText(),
              errorNode.get("code").asInt(),
              errorNode.get("error_message").asText(),
              response
          );
        }
    }

}
