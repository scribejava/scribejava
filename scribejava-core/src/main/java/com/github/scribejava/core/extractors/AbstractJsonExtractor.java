package com.github.scribejava.core.extractors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.exceptions.OAuthException;

public abstract class AbstractJsonExtractor {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected static JsonNode extractRequiredParameter(JsonNode errorNode, String parameterName, String rawResponse)
            throws OAuthException {
        final JsonNode value = errorNode.get(parameterName);

        if (value == null) {
            throw new OAuthException("Response body is incorrect. Can't extract a '" + parameterName
                    + "' from this: '" + rawResponse + "'", null);
        }

        return value;
    }
}
