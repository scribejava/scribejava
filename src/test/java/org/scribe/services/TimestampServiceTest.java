package org.scribe.services;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.*;
import org.scribe.services.TimestampServiceImpl.Timer;

public class TimestampServiceTest
{

  private TimestampServiceImpl service;
  private TimestampServiceImpl.Timer timerStub;

  @Before
  public void setup()
  {
    service = new TimestampServiceImpl();
    timerStub = new TimerStub();
    service.setTimer(timerStub);
  }

  @Test
  public void shouldReturnTimestampInSeconds()
  {
    String expected = "1000";
    assertEquals(expected, service.getTimestampInSeconds());
  }

  @Test
  public void shouldReturnNonce()
  {
    String expected = "1042";
    assertEquals(expected, service.getNonce());
  }

  @Test
  public void testTimerGetMillis()
  {
    Timer timer = new Timer();
    long wnd = 100;
    long now = System.currentTimeMillis();
    long ms = timer.getMilis();
    assertTrue(ms - wnd < now);
    assertTrue(ms + wnd > now);
  }

  @Test
  public void testRandomInteger() {
    Timer timer = new Timer();
    Set<Integer> nums = new HashSet<Integer>();
    int sampleSize = 10000;
    for (int i = 0; i < sampleSize; i++) {
      nums.add(timer.getRandomInteger());
    }
    assertEquals(nums.size(), sampleSize);
  }

  private static class TimerStub extends TimestampServiceImpl.Timer
  {
    @Override
    public Long getMilis()
    {
      return 1000000L;
    }

    @Override
    public Integer getRandomInteger()
    {
      return 42;
    }
  }
}
