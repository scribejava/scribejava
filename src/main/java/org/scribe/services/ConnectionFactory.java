package org.scribe.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public interface ConnectionFactory
{
  HttpURLConnection createConnection(String url) throws MalformedURLException, IOException;
}
