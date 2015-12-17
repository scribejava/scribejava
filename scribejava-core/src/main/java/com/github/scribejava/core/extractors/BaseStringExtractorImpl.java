package com.github.scribejava.core.extractors;

import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Default implementation of {@link BaseStringExtractor}. Conforms to OAuth 1.0a
 *
 * @author Pablo Fernandez
 *
 */
public class BaseStringExtractorImpl implements BaseStringExtractor {

    private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

    /**
     * {@inheritDoc}
     */
    public String extract(AbstractRequest request) {
        checkPreconditions(request);
        String verb = OAuthEncoder.encode(request.getVerb().name());
        String url = OAuthEncoder.encode(request.getSanitizedUrl());
        String params = getSortedAndEncodedParams(request);
        return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
    }

    private String getSortedAndEncodedParams(AbstractRequest request) {
        ParameterList params = new ParameterList();
        params.addAll(request.getQueryStringParams());
        params.addAll(request.getBodyParams());
        params.addAll(new ParameterList(request.getOauthParameters()));
        return params.sort().asOauthBaseString();
    }

    private void checkPreconditions(AbstractRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract base string from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }
}
