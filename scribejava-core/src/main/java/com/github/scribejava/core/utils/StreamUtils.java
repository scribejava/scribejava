package com.github.scribejava.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

/**
 * Utils to deal with Streams.
 */
public abstract class StreamUtils {

    /**
     * Returns the stream contents as an UTF-8 encoded string
     *
     * @param is input stream
     * @return string contents
     * @throws java.io.IOException in any. SocketTimeout in example
     */
    public static String getStreamContents(InputStream is) throws IOException {
        Preconditions.checkNotNull(is, "Cannot get String from a null object");
        final char[] buffer = new char[0x10000];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            int read;
            do {
                read = in.read(buffer, 0, buffer.length);
                if (read > 0) {
                    out.append(buffer, 0, read);
                }
            } while (read >= 0);
        }
        return out.toString();
    }

    /**
     * Return String content from a gzip stream
     *
     * @param is input stream
     * @return string contents
     * @throws java.io.IOException in any. SocketTimeout in example
     */
    public static String getGzipStreamContents(InputStream is) throws IOException {
        Preconditions.checkNotNull(is, "Cannot get String from a null object");
        final GZIPInputStream gis = new GZIPInputStream(is);
        return getStreamContents(gis);
    }

    /**
     * Return String content from a gzip stream. If the stream is not gzipped,
     * it falls back to reading it as a regular stream.
     *
     * @param is input stream
     * @return string contents
     * @throws java.io.IOException in any. SocketTimeout in example
     */
    public static String getGzipStreamContentsWithFallback(InputStream is) throws IOException {
        Preconditions.checkNotNull(is, "Cannot get String from a null object");
        final PushbackInputStream pushback = new PushbackInputStream(is, 2);
        final int firstByte = pushback.read();
        final int secondByte = pushback.read();
        if (secondByte != -1) {
            pushback.unread(secondByte);
        }
        if (firstByte != -1) {
            pushback.unread(firstByte);
        }

        if (firstByte == 0x1f && secondByte == 0x8b) {
            return getGzipStreamContents(pushback);
        }
        return getStreamContents(pushback);
    }

}
