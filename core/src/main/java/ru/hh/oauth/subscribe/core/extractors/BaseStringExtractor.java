package ru.hh.oauth.subscribe.core.extractors;

import ru.hh.oauth.subscribe.core.model.AbstractRequest;

/**
 * Simple command object that extracts a base string from a {@link ru.hh.oauth.subscribe.core.model.AbstractRequest}
 *
 * @author Pablo Fernandez
 */
public interface BaseStringExtractor {

    /**
     * Extracts an url-encoded base string from the {@link ru.hh.oauth.subscribe.core.model.AbstractRequest}.
     *
     * See <a href="http://oauth.net/core/1.0/#anchor14">the oauth spec</a> for more info on this.
     *
     * @param request the OAuthRequest
     * @return the url-encoded base string
     */
    String extract(AbstractRequest request);
}
