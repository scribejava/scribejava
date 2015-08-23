package org.scribe.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.scribe.exceptions.OAuthException;

public class UrlConnectionRequestSender extends RequestSender
{
  public static final UrlConnectionRequestSender INSTANCE = new UrlConnectionRequestSender();

  @Override
  public Response send(Request request, RequestTuner tuner) throws IOException
  {
    HttpURLConnection connection = createConnection(request);
    connection.setRequestMethod(request.getVerb().name());
    if (request.getConnectTimeout() != null)
    {
      connection.setConnectTimeout(request.getConnectTimeout().intValue());
    }
    if (request.getReadTimeout() != null)
    {
      connection.setReadTimeout(request.getReadTimeout().intValue());
    }
    addHeaders(request, connection);
    if (request.getVerb().equals(Verb.PUT) || request.getVerb().equals(Verb.POST))
    {
      addBody(connection, request.getByteBodyContents());
    }
    tuner.tune(request);

    return getResponse(connection);
  }

  HttpURLConnection createConnection(Request request) throws IOException, MalformedURLException
  {
    String completeUrl = request.getCompleteUrl();
    System.setProperty("http.keepAlive", request.isConnectionKeepAlive() ? "true" : "false");
    HttpURLConnection connection = (HttpURLConnection) new URL(completeUrl).openConnection();
    connection.setInstanceFollowRedirects(request.isFollowRedirects());
    return connection;
  }

  private void addHeaders(Request request, HttpURLConnection conn)
  {
    for (String key : request.getHeaders().keySet())
      conn.setRequestProperty(key, request.getHeaders().get(key));
  }

  private void addBody(HttpURLConnection conn, byte[] content) throws IOException
  {
    conn.setRequestProperty(Request.CONTENT_LENGTH, String.valueOf(content.length));

    // Set default content type if none is set.
    if (conn.getRequestProperty(Request.CONTENT_TYPE) == null)
    {
      conn.setRequestProperty(Request.CONTENT_TYPE, Request.DEFAULT_CONTENT_TYPE);
    }
    conn.setDoOutput(true);
    conn.getOutputStream().write(content);
  }

  Response getResponse(HttpURLConnection connection) throws IOException
  {
    try
    {
      connection.connect();
      int code = connection.getResponseCode();
      String message = connection.getResponseMessage();
      Map<String, String> headers = parseHeaders(connection);
      boolean successful = code >= 200 && code < 400;
      InputStream stream = successful ? connection.getInputStream() : connection.getErrorStream();
      return new Response(code, message, stream, headers);
    } catch (UnknownHostException e)
    {
      throw new OAuthException("The IP address of a host could not be determined.", e);
    }
  }

  private Map<String, String> parseHeaders(HttpURLConnection conn)
  {
    Map<String, String> headers = new HashMap<String, String>();
    for (String key : conn.getHeaderFields().keySet())
    {
      headers.put(key, conn.getHeaderFields().get(key).get(0));
    }
    return headers;
  }

}
