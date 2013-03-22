package org.scribe.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.api.Api;
import org.scribe.model.GrantType;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.ResponseType;
import org.scribe.model.SignatureType;
import org.scribe.oauth.OAuthService;

public class ServiceBuilderTest {
	private ServiceBuilder builder;

	@Before
	public void setup() {
		builder = new ServiceBuilder();
	}

	@Test
	public void shouldReturnConfigDefaultValues() {
		builder.provider(ApiMock.class).apiKey("key").apiSecret("secret")
				.build();
		assertEquals(ApiMock.config.getApiKey(), "key");
		assertEquals(ApiMock.config.getApiSecret(), "secret");
		assertEquals(ApiMock.config.getCallback(), OAuthConstants.OUT_OF_BAND);
		assertEquals(ApiMock.config.getGrantType(),
				GrantType.AUTHORIZATION_CODE);
		assertEquals(ApiMock.config.getResponseType(), ResponseType.CODE);
		assertEquals(ApiMock.config.getSignatureType(),
				SignatureType.QUERY_STRING);
	}

	@Test
	public void shouldAcceptValidCallbackUrl() {
		builder.provider(ApiMock.class).apiKey("key").apiSecret("secret")
				.callback("http://example.com").build();
		assertEquals(ApiMock.config.getApiKey(), "key");
		assertEquals(ApiMock.config.getApiSecret(), "secret");
		assertEquals(ApiMock.config.getCallback(), "http://example.com");
	}

	@Test
	public void shouldAcceptAGrantTypeType() {
		builder.provider(ApiMock.class).apiKey("key").apiSecret("secret")
				.grantType(GrantType.REFRESH_TOKEN).build();
		assertEquals(ApiMock.config.getApiKey(), "key");
		assertEquals(ApiMock.config.getApiSecret(), "secret");
		assertEquals(ApiMock.config.getGrantType(), GrantType.REFRESH_TOKEN);
	}

	@Test
	public void shouldAcceptAResponseType() {
		builder.provider(ApiMock.class).apiKey("key").apiSecret("secret")
				.responseType(ResponseType.TOKEN).build();
		assertEquals(ApiMock.config.getApiKey(), "key");
		assertEquals(ApiMock.config.getApiSecret(), "secret");
		assertEquals(ApiMock.config.getResponseType(), ResponseType.TOKEN);
	}

	@Test
	public void shouldAcceptASignatureType() {
		builder.provider(ApiMock.class).apiKey("key").apiSecret("secret")
				.signatureType(SignatureType.QUERY_STRING).build();
		assertEquals(ApiMock.config.getApiKey(), "key");
		assertEquals(ApiMock.config.getApiSecret(), "secret");
		assertEquals(ApiMock.config.getSignatureType(),
				SignatureType.QUERY_STRING);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAcceptNullAsCallback() {
		builder.provider(ApiMock.class).apiKey("key").apiSecret("secret")
				.callback(null).build();
	}

	@Test
	public void shouldAcceptAnScope() {
		builder.provider(ApiMock.class).apiKey("key").apiSecret("secret")
				.scope("rss-api").build();
		assertEquals(ApiMock.config.getApiKey(), "key");
		assertEquals(ApiMock.config.getApiSecret(), "secret");
		assertEquals(ApiMock.config.getScope(), "rss-api");
	}

	public static class ApiMock implements Api {
		public static OAuthConfig config;

		public OAuthService createService(OAuthConfig config) {
			ApiMock.config = config;
			return null;
		}
	}
}
