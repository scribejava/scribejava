package com.github.scribejava.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class StreamUtilsTest {

    private static final InputStream ALLWAYS_ERROR_INPUT_STREAM = new AllwaysErrorInputStream();

    private static class AllwaysErrorInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            throw new IOException();
        }
    }

    @Test
    public void shouldCorrectlyDecodeAStream() throws IOException {
        final String value = "expected";
        final InputStream is = new ByteArrayInputStream(value.getBytes());
        final String decoded = StreamUtils.getStreamContents(is);
        assertEquals("expected", decoded);
    }

    public void shouldFailForNullParameter() throws IOException {
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                StreamUtils.getStreamContents(null);
            }
        });
    }

    public void shouldFailWithBrokenStream() throws IOException {
        assertThrows(IOException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                // This object simulates problems with input stream.
                StreamUtils.getStreamContents(ALLWAYS_ERROR_INPUT_STREAM);
            }
        });
    }
}
