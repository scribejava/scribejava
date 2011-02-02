package org.scribe.utils;

import java.io.*;

/**
 * Utils to deal with Streams.
 *  
 * @author Pablo Fernandez
 */
public class StreamUtils
{
  /**
   * Returns the stream contents as an UTF-8 encoded string
   * 
   * @param in input stream
   * @return stream contents
   */
  public static byte[] getStreamContents(InputStream in)
  {
    Preconditions.checkNotNull(in, "Cannot get String from a null object");
    ByteArrayOutputStream out = null;
    try
    {
      final byte[] buffer = new byte[0x10000];
      out = new ByteArrayOutputStream();
      int read;
      while (0 < (read = in.read(buffer, 0, buffer.length)))
        out.write(buffer, 0, read);
      return out.toByteArray();
    } catch (IOException ioe)
    {
      throw new IllegalStateException("Error while reading response body", ioe);
    } finally {
      close(in);
      close(out);
    }
  }

  /**
   * Closes any {@link Closeable} while quashing any exceptions.
   * @param closeable the {@link Closeable} to close (may be {@code null}).
   */
  public static void close(Closeable closeable) {
    if (closeable != null)
    {
      try
      {
        closeable.close();
      } catch (IOException e)
      {
        // ignore
      }
    }
  }
}
