package org.scribe.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DefaultConnectionFactory implements ConnectionFactory
{
  public HttpURLConnection createConnection(String url) throws MalformedURLException, IOException
  {
    return (HttpURLConnection) new URL(url).openConnection();
  }
}
