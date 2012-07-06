/**
 * 
 */
package org.scribe.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.exceptions.OAuthException;

/**
 * Represents an asynchronous HTTP Request object.
 * 
 * Because this implementation uses an HTTP request pool, timeouts are controlled
 * at a global level, rather than per-request.  The following JVM system properties
 * control the behavior of the pool:
 * <ul>
 *   <li>org.scribe.async.socket.timeout - overall socket idle (keep-alive) timeout in milliseconds, default is 30 seconds</li>
 *   <li>org.scribe.async.connect.timeout - socket connect timeout in milliseconds, default is 30 seconds</li>
 *   <li>org.scribe.async.socket.buffer - socket buffer size in bytes, default is 8Kb (8192 bytes)</li>
 *   <li>org.scribe.async.tcp.nodelay - true to use TCP_NODELAY, false to disable, default is true
 * </ul>
 * @author Brett Wooldridge
 */
public class RequestAsync extends RequestBase
{
  private static HttpAsyncRequester requester;
  private static BasicNIOConnPool pool;

  static
  {
    // HTTP parameters for the client
    final HttpParams params = new BasicHttpParams();
    params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.getInteger("org.scribe.async.socket.timeout", 30000));
    params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.getInteger("org.scribe.async.connect.timeout", 30000));
    params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, Integer.getInteger("org.scribe.async.socket.buffer", 8 * 1024));
    params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, Boolean.valueOf(System.getProperty("org.scribe.async.tcp.nodelay", "true")));
    params.setParameter(CoreProtocolPNames.USER_AGENT, "Scribe/1.1");

    // Create client-side I/O reactor
    final ConnectingIOReactor ioReactor;
    try
    {
      IOReactorConfig config = new IOReactorConfig();
      config.setIoThreadCount(1);
      ioReactor = new DefaultConnectingIOReactor(config);
    }
    catch (IOReactorException e)
    {
      throw new OAuthException("Exception initializing asynchronous Apache HTTP Client", e);
    }

    // Create HTTP connection pool
    pool = new BasicNIOConnPool(ioReactor, params);
    // Limit total number of connections to just two
    pool.setDefaultMaxPerRoute(2);
    pool.setMaxTotal(2);

    // Run the I/O reactor in a separate thread
    Thread t = new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          // Create client-side HTTP protocol handler
          HttpAsyncRequestExecutor protocolHandler = new HttpAsyncRequestExecutor();
          // Create client-side I/O event dispatch
          IOEventDispatch ioEventDispatch = new DefaultHttpClientIODispatch(protocolHandler, params);
          // Ready to go!
          ioReactor.execute(ioEventDispatch);
        }
        catch (InterruptedIOException ex)
        {
          System.err.println("Interrupted");
        }
        catch (IOException e)
        {
          System.err.println("I/O error: " + e.getMessage());
        }
      }

    }, "Scribe/Apache HTTP IOReactor");
    // Start the client thread
    t.setDaemon(true);
    t.start();

    // Create HTTP protocol processing chain
    HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
        // Use standard client-side protocol interceptors
        new RequestContent(),
        new RequestTargetHost(),
        new RequestConnControl(),
        new RequestUserAgent(),
        new RequestExpectContinue() });

    // Create HTTP requester
    requester = new HttpAsyncRequester(httpproc, new DefaultConnectionReuseStrategy(), params);
  }

  /**
   * Creates a new Http Request
   * 
   * @param verb Http Verb (GET, POST, etc)
   * @param url url with optional querystring parameters.
   */
  public RequestAsync(Verb verb, String url)
  {
    this.verb = verb;
    this.url = url;
  }

  /**
   * {@inheritDoc}
   */
  public Response send()
  {
    throw new OAuthException("Attempted to call send() method on an asynchronous request, use sendAsync() instead");
  }

  /**
   * {@inheritDoc}
   */
  public void sendAsync(ResponseCallback callBack)
  {
    URL connectUrl;
    try
    {
      connectUrl = new URL(url);
    }
    catch (MalformedURLException e)
    {
      throw new OAuthConnectionException(e);
    }

    String path = connectUrl.getPath() + (connectUrl.getQuery() != null ? connectUrl.getQuery() : "");
    int port = connectUrl.getPort() != -1 ? connectUrl.getPort() : connectUrl.getDefaultPort();

    HttpRequest request;
    if (verb.equals(Verb.PUT) || verb.equals(Verb.POST))
    {
      request = new BasicHttpEntityEnclosingRequest(verb.name(), path);
      addBody(request, getByteBodyContents());
    }
    else
    {
      request = new BasicHttpRequest(verb.name(), path);
    }
    addHeaders(request);

    HttpHost target = new HttpHost(connectUrl.getHost(), port, connectUrl.getProtocol());
    
    BasicAsyncRequestProducer asyncRequestProducer = new BasicAsyncRequestProducer(target, request);
    BasicAsyncResponseConsumer asyncResponseConsumer = new BasicAsyncResponseConsumer();
    BasicHttpContext httpContext = new BasicHttpContext();

    RequestAsyncFuture futureCallback = new RequestAsyncFuture(callBack);

    requester.execute(asyncRequestProducer, asyncResponseConsumer, pool, httpContext, futureCallback);
  }

  /**
   * Set the request headers.
   *
   * @param request the request object
   */
  private void addHeaders(HttpRequest request)
  {
    for (String key : headers.keySet())
    {
      request.addHeader(key, headers.get(key));
    }
  }

  /**
   * Add the body content to the request.
   *
   * @param request the request object
   * @param content an array of bytes representing the content to PUT or POST
   */
  void addBody(HttpRequest request, byte[] content)
  {
    HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;

    HttpEntity entity;
    if (entityRequest.getFirstHeader(CONTENT_TYPE) == null)
    {
      // Set default content type if none is set.
      entity = new ByteArrayEntity(content, ContentType.APPLICATION_FORM_URLENCODED);
    }
    else
    {
      entity = new ByteArrayEntity(content);
    }

    entityRequest.setEntity(entity);
  }

  /**
   * Private class to handle the HttpResponse callback.
   *
   */
  private class RequestAsyncFuture implements FutureCallback<HttpResponse>
  {
    private ResponseCallback callBack;

    RequestAsyncFuture(ResponseCallback callBack)
    {
      this.callBack = callBack;
    }

    public void completed(final HttpResponse response)
    {
      Map<String, String> headers = new HashMap<String, String>();
      HeaderIterator headerIterator = response.headerIterator();
      while (headerIterator.hasNext())
      {
        Header header = headerIterator.nextHeader();
        headers.put(header.getName(), header.getValue());
      }

      StatusLine statusLine = response.getStatusLine();
      int statusCode = statusLine.getStatusCode();
      
      InputStream stream;
      if (statusCode >= 200 && statusCode < 400)
      {
        HttpEntity entity = response.getEntity();
        try
        {
          stream = entity.getContent();
        }
        catch (Exception e)
        {
          failed(e);
          return;
        }
      }
      else
      {
        stream = new ByteArrayInputStream(statusLine.getReasonPhrase().getBytes());
      }

      callBack.onResponse(new Response(statusCode, headers, stream));
    }

    public void failed(final Exception ex)
    {
      callBack.onError(new OAuthException(ex));
    }

    public void cancelled()
    {
      callBack.onError(new OAuthException("HTTP Request was cancelled"));
    }
  }
}
