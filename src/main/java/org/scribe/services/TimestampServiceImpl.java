package org.scribe.services;

import java.util.*;

public class TimestampServiceImpl implements TimestampService
{

  private Timer timer;

  public TimestampServiceImpl()
  {
    timer = new Timer();
  }

  public String getNonce()
  {
    Long ts = getTs();
    return String.valueOf(ts + timer.getRandomInteger());
  }

  public String getTimestampInSeconds()
  {
    return String.valueOf(getTs());
  }

  private Long getTs()
  {
    return timer.getMilis() / 1000;
  }

  void setTimer(Timer timer)
  {
    this.timer = timer;
  }

  static class Timer
  {
    public Long getMilis()
    {
      return System.currentTimeMillis();
    }

    public Integer getRandomInteger()
    {
      return new Random().nextInt();
    }
  }

}
