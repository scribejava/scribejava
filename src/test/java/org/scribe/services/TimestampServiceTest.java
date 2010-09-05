package org.scribe.services;

import static org.junit.Assert.*;

import org.junit.*;

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
