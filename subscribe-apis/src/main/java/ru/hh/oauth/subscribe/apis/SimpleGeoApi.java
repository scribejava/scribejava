package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi10a;
import ru.hh.oauth.subscribe.core.model.Token;

/**
 * @author Pablo Fernandez
 */
public class SimpleGeoApi extends DefaultApi10a {

    private static final String ENDPOINT = "these are not used since SimpleGeo uses 2 legged OAuth";

    @Override
    public String getRequestTokenEndpoint() {
        return ENDPOINT;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ENDPOINT;
    }

    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        return ENDPOINT;
    }
}
