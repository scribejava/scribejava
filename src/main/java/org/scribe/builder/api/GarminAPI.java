import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Created with IntelliJ IDEA.
 * User: khanhnguyen
 * Date: 04.06.14
 * Time: 17:04
 */
public class GarminAPI extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "http://connecttest.garmin.com/oauthConfirm?oauth_token=%s";
    private static final String REQUEST_TOKEN_RESOURCE = "gcsapitest.garmin.com/gcs-api/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "gcsapitest.garmin.com/gcs-api/oauth/access_token";

    @Override
    public String getAccessTokenEndpoint()
    {
        return "https://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint()
    {
        return "https://" + REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

}
