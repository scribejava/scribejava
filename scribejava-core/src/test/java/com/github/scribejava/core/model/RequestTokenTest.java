package com.github.scribejava.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;

public class RequestTokenTest {

    @Test
    public void shouldTestEqualityBasedOnTokenAndSecret() {
        final RequestToken expected = new RequestToken("access", "secret");
        final RequestToken actual = new RequestToken("access", "secret");

        assertEquals(expected, actual);
        assertEquals(actual, actual);
    }

    @Test
    public void shouldNotDependOnRawString() {
        final RequestToken expected = new RequestToken("access", "secret", "raw_string");
        final RequestToken actual = new RequestToken("access", "secret", "different_raw_string");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnSameHashCodeForEqualObjects() {
        final RequestToken expected = new RequestToken("access", "secret");
        final RequestToken actual = new RequestToken("access", "secret");

        assertEquals(expected.hashCode(), actual.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherObjects() {
        final RequestToken expected = new RequestToken("access", "secret", "response");

        assertNotSame(expected, null);
        assertNotSame(expected, new Object());
    }

    @Test
    public void shouldReturnUrlParam() throws Exception {
        RequestToken actual = new RequestToken("acccess", "secret", "user_id=3107154759&screen_name=someuser&empty=&=");
        assertEquals("someuser", actual.getParameter("screen_name"));
        assertEquals("3107154759", actual.getParameter("user_id"));
        assertEquals(null, actual.getParameter("empty"));
        assertEquals(null, actual.getParameter(null));
    }
}
