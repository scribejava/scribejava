package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;

public class TokenTest {

    @Test
    public void shouldTestEqualityBasedOnTokenAndSecret() {
        final Token expected = new Token("access", "secret");
        final Token actual = new Token("access", "secret");

        assertEquals(expected, actual);
        assertEquals(actual, actual);
    }

    @Test
    public void shouldNotDependOnRawString() {
        final Token expected = new Token("access", "secret", "raw_string");
        final Token actual = new Token("access", "secret", "different_raw_string");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnSameHashCodeForEqualObjects() {
        final Token expected = new Token("access", "secret");
        final Token actual = new Token("access", "secret");

        assertEquals(expected.hashCode(), actual.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherObjects() {
        final Token expected = new Token("access", "secret", "response");

        assertNotSame(expected, null);
        assertNotSame(expected, new Object());
    }
}
