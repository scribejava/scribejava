package org.scribe.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.scribe.utils.StreamUtils;

/**
 * Represents an HTTP Response.
 * 
 * @author Pablo Fernandez
 */
public class Response
{
  private int code;
  private String message;
  private String body;
  private InputStream stream;
  private Map<String, String> headers;

  Response(int code, String message, InputStream stream, Map<String, String> headers) throws IOException
  {
    this.code = code;
    this.message = message;
    this.stream = stream;
    this.headers = headers;
  }

  private String parseBodyContents()
  {
    body = StreamUtils.getStreamContents(getStream());
    return body;
  }

  public boolean isSuccessful()
  {
    return getCode() >= 200 && getCode() < 400;
  }

  /**
   * Obtains the HTTP Response body
   * 
   * @return response body
   */
  public String getBody()
  {
    return body != null ? body : parseBodyContents();
  }

  /**
   * Obtains the meaningful stream of the HttpUrlConnection, either inputStream
   * or errorInputStream, depending on the status code
   * 
   * @return input stream / error stream
   */
  public InputStream getStream()
  {
    return stream;
  }

  /**
   * Obtains the HTTP status code
   * 
   * @return the status code
   */
  public int getCode()
  {
    return code;
  }
  
  /**
   * Obtains the HTTP status message.
   * Returns <code>null</code> if the message can not be discerned from the response (not valid HTTP)
   * 
   * @return the status message
   */
  public String getMessage() 
  {
    return message;
  }

  /**
   * Obtains a {@link Map} containing the HTTP Response Headers
   * 
   * @return headers
   */
  public Map<String, String> getHeaders()
  {
    return headers;
  }

  /**
   * Obtains a single HTTP Header value, or null if undefined
   * 
   * @param name the header name.
   * 
   * @return header value or null.
   */
  public String getHeader(String name)
  {
    return headers.get(name);
  }

}