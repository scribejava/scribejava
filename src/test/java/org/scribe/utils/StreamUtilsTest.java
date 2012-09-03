package org.scribe.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import static org.junit.Assert.*;

public class StreamUtilsTest
{

  /**
   * the last bit to reach 100% coverage
   * @throws InvocationTargetException on error
   * @throws IllegalAccessException on error
   * @throws InstantiationException on error
   * @throws IllegalArgumentException on error
   */
  @Test
  public void ctorCoverage() throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Constructor<?> ctor = StreamUtils.class.getDeclaredConstructors()[0];
    ctor.setAccessible(true);
    ctor.newInstance((Object[]) null);
  }

  @Test
  public void shouldCorrectlyDecodeAStream()
  {
    String value = "expected";
    InputStream is = new ByteArrayInputStream(value.getBytes());
    String decoded = StreamUtils.getStreamContents(is);
    assertEquals("expected", decoded);
  }

  @Test(expected=IllegalStateException.class)
  public void testWithInvalidInputStream()
  {
    StreamUtils.getStreamContents(new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("fail");
      }
    });
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailForNullParameter()
  {
    InputStream is = null;
    StreamUtils.getStreamContents(is);
    fail("Must throw exception before getting here");
  }
}
