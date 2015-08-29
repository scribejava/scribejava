package org.scribe.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class UrlConnectionRequestSenderStub extends UrlConnectionRequestSender
{
  private ConnectionStub connection;

  public UrlConnectionRequestSenderStub(ConnectionStub connection)
  {
    this.connection = connection;
  }

  @Override
  HttpURLConnection createConnection(Request request) throws IOException, MalformedURLException
  {
    return connection;
  }
}
