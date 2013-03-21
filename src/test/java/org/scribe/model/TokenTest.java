package org.scribe.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

import org.junit.Test;

public class TokenTest {
	@Test
	public void shouldTestEqualityBasedOnTokenAndSecret() throws Exception {
		OAuthToken expected = new OAuthToken("access", "secret");
		OAuthToken actual = new OAuthToken("access", "secret");

		assertEquals(expected, actual);
		assertEquals(actual, actual);
	}

	@Test
	public void shouldNotDependOnRawString() throws Exception {
		OAuthToken expected = new OAuthToken("access", "secret", "raw_string");
		OAuthToken actual = new OAuthToken("access", "secret", "different_raw_string");

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnSameHashCodeForEqualObjects() throws Exception {
		OAuthToken expected = new OAuthToken("access", "secret");
		OAuthToken actual = new OAuthToken("access", "secret");

		assertEquals(expected.hashCode(), actual.hashCode());
	}

	@Test
	public void shouldNotBeEqualToNullOrOtherObjects() throws Exception {
		OAuthToken expected = new OAuthToken("access", "secret", "response");

		assertNotSame(expected, null);
		assertNotSame(expected, new Object());
	}
}
