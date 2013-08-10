package org.scribe.builder.api;

import static org.junit.Assert.*;
import org.junit.*;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.*;

/**
 * @author Aleksey Leshko
 */

public class Google20ApiTest {
	DefaultApi20 api;

	@Before
	public void setup() throws Exception
	{
		api = new Google20Api();
	}

	@Test
	public void shouldGetAuthorizationUrl()
	{
		OAuthConfig oAuthConfig = createOAuthConfig();
		String authorizationUrl = api.getAuthorizationUrl(oAuthConfig);

		String expected = "https://accounts.google.com/o/oauth2/auth?";
		expected += "&" + OAuthConstants.REDIRECT_URI + "=" + oAuthConfig.getCallback();
		expected += "&" + OAuthConstants.CLIENT_ID + "=" + oAuthConfig.getApiKey();
		expected += "&" + OAuthConstants.SCOPE + "=" + oAuthConfig.getScope();
		expected += "&response_type=code";
		expected += "&approval_prompt=force";
		expected += "&access_type=offline";
		assertEquals(expected, authorizationUrl);
	}

	@Test
	public void shouldGetAccessTokenEndpoint()
	{
		assertEquals("https://accounts.google.com/o/oauth2/token", api.getAccessTokenEndpoint());
	}

	@Test
	public void shouldGetAccessTokenVerb()
	{
		assertEquals(Verb.POST, api.getAccessTokenVerb());
	}

	@Test
	public void shouldGetAccessTokenExtractor()
	{
		assertEquals(JsonTokenExtractor.class, api.getAccessTokenExtractor().getClass());
	}

	@Test
	public void shouldGetParameterList()
	{
		ParameterList parameterList = api.getParameterList();

		assertEquals(1, parameterList.size());
		assertTrue(parameterList.contains(new Parameter("grant_type", "authorization_code")));

	}

	private OAuthConfig createOAuthConfig() {
		String key = "key";
		String secret = "secret";
		String callback = "callback";
		String scope = "scope";

		return new OAuthConfig(key, secret, callback, null, scope, null);
	}
}
