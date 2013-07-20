package org.scribe.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RequestTuner
{
  public abstract void tune(Request request);

  public static RequestTuner[] append(RequestTuner[] existingTuners, RequestTuner[] tuners, RequestTuner defaultTuner)
  {
    List<RequestTuner> requestTuners = new ArrayList<RequestTuner>();
    if (existingTuners == null)
    {
      requestTuners.addAll(Arrays.asList(tuners));
    }
    else
    {
      requestTuners.addAll(Arrays.asList(existingTuners));
      requestTuners.addAll(Arrays.asList(tuners));
    }
    if (tuners.length == 0)
    {
      requestTuners.add(defaultTuner);
    }
    return requestTuners.toArray(new RequestTuner[requestTuners.size()]);
  }
}