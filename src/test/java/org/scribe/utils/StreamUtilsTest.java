package org.scribe.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class StreamUtilsTest {

	@Test
	public void shouldCorrectlyDecodeAStream() {
		String value = "expected";
		InputStream is = new ByteArrayInputStream(value.getBytes());
		String decoded = StreamUtils.getStreamContents(is);
		assertEquals("expected", decoded);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailForNullParameter() {
		InputStream is = null;
		StreamUtils.getStreamContents(is);
		fail("Must throw exception before getting here");
	}
}
