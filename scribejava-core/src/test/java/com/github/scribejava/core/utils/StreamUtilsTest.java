package com.github.scribejava.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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

    @Test(expected = IllegalStateException.class)
    public void shouldFailWithBrokenStream() {
        // This object simulates problems with input stream.
        final InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        StreamUtils.getStreamContents(is);
        fail("Must throw exception before getting here");
    }
}
