package org.scribe.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

/**
 * Implementation of {@link RequestSender} which is base on the Apache HttpClient. <br />
 * Due to the design of the HttpClient, setting timeouts on the {@link Request} has not effect; it should be handled via
 * the configuration of the {@link HttpClient}.
 */
public class HttpClientRequestSender extends RequestSender
{
  private HttpClient client;

  public HttpClientRequestSender(HttpClient client)
  {
    this.client = client;
  }

  @Override
  public Response send(Request request, RequestTuner tuner) throws IOException
  {
    tuner.tune(request);
    HttpUriRequest req = createRequest(request);
    addHeaders(request, req);
    if (req instanceof HttpEntityEnclosingRequest)
    {
      addBody((HttpEntityEnclosingRequest) req, request.getByteBodyContents());
    }

    HttpResponse resp = client.execute(req);
    return getResponse(resp);
  }

  private HttpUriRequest createRequest(Request request)
  {
    String completeUrl = request.getCompleteUrl();
    HttpUriRequest req;
    switch (request.getVerb())
    {
      case GET:
        req = new HttpGet(completeUrl);
        break;
      case POST:
        req = new HttpGet(completeUrl);
        break;
      case DELETE:
        req = new HttpDelete(completeUrl);
        break;
      case HEAD:
        req = new HttpHead(completeUrl);
        break;
      case OPTIONS:
        req = new HttpOptions(completeUrl);
        break;
      case PUT:
        req = new HttpPut(completeUrl);
        break;
      case TRACE:
        req = new HttpTrace(completeUrl);
        break;
      default:
        throw new IllegalStateException("Unexpected HTTP verb : " + request.getVerb());
    }
    return req;
  }

  private void addHeaders(Request request, HttpUriRequest r)
  {
    for (String key : request.getHeaders().keySet())
      r.addHeader(key, request.getHeaders().get(key));
  }

  private void addBody(HttpEntityEnclosingRequest r, byte[] content) throws IOException
  {
    HttpEntity entity = new ByteArrayEntity(content, ContentType.APPLICATION_FORM_URLENCODED);
    r.setEntity(entity);
  }

  private Response getResponse(HttpResponse resp) throws IOException
  {
    int code = resp.getStatusLine().getStatusCode();
    String message = resp.getStatusLine().getReasonPhrase();
    Map<String, String> headers = parseHeaders(resp);
    return new Response(code, message, resp.getEntity().getContent(), headers);
  }

  private Map<String, String> parseHeaders(HttpResponse resp)
  {
    Map<String, String> headers = new HashMap<String, String>();
    for (Header header : resp.getAllHeaders())
    {
      headers.put(header.getName(), header.getValue());
    }
    return headers;
  }

}
