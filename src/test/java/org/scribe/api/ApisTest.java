package org.scribe.api;


import junit.framework.TestCase;
import org.junit.Test;
import org.scribe.builder.api.GoogleApi20;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;

public class ApisTest
    extends TestCase
{
    @Test
    public void testGoogleApi20()
    {
        GoogleApi20 api = new GoogleApi20();
        assertEquals( OAuthConstants.TOKEN, api.getTokenParameterName() );
        assertEquals( "https://accounts.google.com/o/oauth2/token", api.getAccessTokenEndpoint() );
        assertEquals( Verb.POST, api.getAccessTokenVerb() );
        assertEquals( JsonTokenExtractor.class, api.getAccessTokenExtractor().getClass() );

        OAuthConfig config = new OAuthConfig( "key", null, "callback", null, "scope", null );
        assertEquals(
            "https://accounts.google.com/o/oauth2/auth?client_id=keyq&redirect_uri=callback&scope=scope&response_type=code",
            api.getAuthorizationUrl( config ) );
    }
}
