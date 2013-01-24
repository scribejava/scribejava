package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * @author Candide Kemmler (candide@fluxtream.com)
 */
public class WithingsApi extends DefaultApi10a {

    @Override
    public String getRequestTokenEndpoint() {
        return "https://oauth.withings.com/account/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth.withings.com/account/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return "https://oauth.withings.com/account/authorize?oauth_token=" + requestToken.getToken();
    }
}
