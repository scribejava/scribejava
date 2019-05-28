package com.github.scribejava.apis.salesforce;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

/**
 * This extractor parses in addition to the standard Extractor the instance_url
 * of the used Salesforce organization.
 */
public class SalesforceJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    protected SalesforceJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final SalesforceJsonTokenExtractor INSTANCE = new SalesforceJsonTokenExtractor();
    }

    public static SalesforceJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected SalesforceToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JsonNode response, String rawResponse) {
        return new SalesforceToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractRequiredParameter(response, "instance_url", rawResponse).asText(), rawResponse);
    }
}
