package org.scribe.model;

import java.io.IOException;

public abstract class RequestSender
{

  public abstract Response send(Request request, RequestTuner tuner) throws IOException;

}
