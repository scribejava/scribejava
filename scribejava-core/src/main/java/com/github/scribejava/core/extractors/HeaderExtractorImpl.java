package com.github.scribejava.core.extractors;

import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

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

        final StringBuilder header = new StringBuilder(PREAMBLE);

        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            if (header.length() > PREAMBLE.length()) {
                header.append(PARAM_SEPARATOR);
            }
            header.append(parameter.getKey())
                    .append("=\"")
                    .append(OAuthEncoder.encode(parameter.getValue()))
                    .append('"');
        }

        if (request.getRealm() != null && !request.getRealm().isEmpty()) {
            header.append(PARAM_SEPARATOR)
                    .append(OAuthConstants.REALM)
                    .append("=\"")
                    .append(request.getRealm())
                    .append('"');
        }
        return header.toString();
    }

    private void checkPreconditions(OAuthRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract a header from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }

}
