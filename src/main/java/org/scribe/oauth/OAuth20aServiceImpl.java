package org.scribe.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.scribe.builder.api.DefaultApi20a;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;

/**
 * User: ezgraphs
 * Date: 12/27/11
 * Time: 11:16 AM
 */
public class OAuth20aServiceImpl extends OAuth20ServiceImpl implements OAuthService {

    private final DefaultApi20a api;
    private final OAuthConfig config;

    /**
     * Replicate api and config variables and assignment
     * since the superclass declares these private
     *
     * @param api
     * @param config
     */
    public OAuth20aServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = (DefaultApi20a) api;
        this.config = config;
    }

    /**
     * The method overwritten below was required because parameters are body parameters in the
     * Google version of the API.  In addition, a body parameter of type grant_type is needed.
     *
     * {@inheritDoc}
     */
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());

        request.addBodyParameter("grant_type", "authorization_code");
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
        Response response = request.send();
        String body = response.getBody();
        System.out.println("body:\n" + body);
        return extract(body);
    }

    /**
     * @param body
     *
     * @return
     */
    private Token extract(String body) {
        JsonParser parser = new JsonParser();
        JsonElement e = parser.parse(body);

        String accessToken = e.getAsJsonObject().get("access_token").getAsString();
        String id = e.getAsJsonObject().get("id_token").getAsString();
        return new Token(accessToken, id);
    }
}