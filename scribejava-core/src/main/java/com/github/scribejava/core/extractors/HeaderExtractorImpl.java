package com.github.scribejava.core.extractors;

import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Default implementation of {@link HeaderExtractor}. Conforms to OAuth 1.0a
 *
 * @author Pablo Fernandez
 *
 */
public class HeaderExtractorImpl implements HeaderExtractor {

    private static final String PARAM_SEPARATOR = ", ";
    private static final String PREAMBLE = "OAuth ";
    public static final int ESTIMATED_PARAM_LENGTH = 20;

    /**
     * {@inheritDoc}
     */
    @Override
    public String extract(final AbstractRequest request) {
        checkPreconditions(request);
        final Map<String, String> parameters = request.getOauthParameters();
        final StringBuilder header = new StringBuilder(parameters.size() * ESTIMATED_PARAM_LENGTH);
        header.append(PREAMBLE);
        for (final Map.Entry<String, String> entry : parameters.entrySet()) {
            if (header.length() > PREAMBLE.length()) {
                header.append(PARAM_SEPARATOR);
            }
            header.append(String.format("%s=\"%s\"", entry.getKey(), OAuthEncoder.encode(entry.getValue())));
        }

        if (request.getRealm() != null && !request.getRealm().isEmpty()) {
            header.append(PARAM_SEPARATOR);
            header.append(String.format("%s=\"%s\"", OAuthConstants.REALM, request.getRealm()));
        }

        return header.toString();
    }

    private void checkPreconditions(final AbstractRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract a header from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }

}
