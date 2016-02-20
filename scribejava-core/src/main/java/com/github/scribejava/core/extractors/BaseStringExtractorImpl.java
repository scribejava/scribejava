package com.github.scribejava.core.extractors;

import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Default implementation of {@link BaseStringExtractor}. Conforms to OAuth 1.0a
 */
public class BaseStringExtractorImpl implements BaseStringExtractor {

    protected static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

    /**
     * {@inheritDoc}
     */
    @Override
    public String extract(AbstractRequest request) {
        checkPreconditions(request);
        final String verb = OAuthEncoder.encode(getVerb(request));
        final String url = OAuthEncoder.encode(getUrl(request));
        final String params = getSortedAndEncodedParams(request);
        return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
    }

    protected String getVerb(AbstractRequest request) {
        return request.getVerb().name();
    }

    protected String getUrl(AbstractRequest request) {
        return request.getSanitizedUrl();
    }

    protected String getSortedAndEncodedParams(AbstractRequest request) {
        final ParameterList params = new ParameterList();
        params.addAll(request.getQueryStringParams());
        params.addAll(request.getBodyParams());
        params.addAll(new ParameterList(request.getOauthParameters()));
        return params.sort().asOauthBaseString();
    }

    protected void checkPreconditions(AbstractRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract base string from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }
}
