package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.extractors.AccessTokenExtractor;
import ru.hh.oauth.subscribe.core.extractors.JsonTokenExtractor;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;

/**
 * @author Boris G. Tsirkin &lt;mail@dotbg.name&gt;
 * @since 20.4.2011
 */
public class VkontakteApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = String.format("%s&scope=%%s", AUTHORIZE_URL);

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth.vk.com/access_token";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Valid url is required for a callback. Vkontakte does not support OOB");
        if (config.hasScope()) { // Appending scope if present
            return String.format(
                    SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
