package com.github.scribejava.core.extractors;

import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link HeaderExtractor}. Conforms to OAuth 1.0a
 */
public class HeaderExtractorImpl implements HeaderExtractor {

    private static final String PARAM_SEPARATOR = ", ";
    private static final String PREAMBLE = "OAuth ";

    /**
     * {@inheritDoc}
     */
    @Override
    public String extract(OAuthRequest request) {
        checkPreconditions(request);
        final Map<String, String> parameters = request.getOauthParameters();

        final String paramsString = parameters.entrySet().stream()
                .map(entry -> String.format("%s=\"%s\"", entry.getKey(), OAuthEncoder.encode(entry.getValue())))
                .collect(Collectors.joining(PARAM_SEPARATOR, PREAMBLE, ""));

        if (request.getRealm() == null || request.getRealm().isEmpty()) {
            return paramsString;
        }

        return paramsString + PARAM_SEPARATOR + String.format("%s=\"%s\"", OAuthConstants.REALM, request.getRealm());
    }

    private void checkPreconditions(OAuthRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract a header from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }

}
