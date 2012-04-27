package org.scribe.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.scribe.exceptions.OAuthException;

public class URLConnectionStrategy implements HttpStrategy {
    protected static final String CONTENT_LENGTH = "Content-Length";
    protected static final String CONTENT_TYPE = "Content-Type";

    public Response send(Request request) {
        try {
            HttpURLConnection connection = createConnection(request);
            return doSend(request, connection);
        } catch (UnknownHostException uhe) {
            throw new OAuthException(
                    "Could not reach the desired host. Check your network connection.", uhe);
        } catch (IOException ioe) {
            throw new OAuthException("Problems while creating connection.", ioe);
        }
    }

    protected HttpURLConnection createConnection(Request request) throws IOException {
        String completeUrl = request.getCompleteUrl();
        System.setProperty("http.keepAlive", request.isConnectionKeepAlive() ? "true" : "false");
        return (HttpURLConnection) new URL(completeUrl).openConnection();
    }

    protected Response doSend(Request request, HttpURLConnection connection) throws IOException {
        connection.setRequestMethod(request.getVerb().name());
        if (request.getConnectTimeout() != null) {
            connection.setConnectTimeout(request.getConnectTimeout().intValue());
        }
        if (request.getReadTimeout() != null) {
            connection.setReadTimeout(request.getReadTimeout().intValue());
        }
        addHeaders(request, connection);
        if (request.getVerb().equals(Verb.PUT) || request.getVerb().equals(Verb.POST)) {
            addBody(connection, request.getByteBodyContents());
        }
        return new Response(connection);
    }

    protected void addHeaders(Request request, HttpURLConnection conn) {
        for (String key : request.getHeaders().keySet())
            conn.setRequestProperty(key, request.getHeaders().get(key));
    }

    protected void addBody(HttpURLConnection conn, byte[] content) throws IOException {
        conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));

        // Set default content type if none is set.
        if (conn.getRequestProperty(CONTENT_TYPE) == null) {
            conn.setRequestProperty(CONTENT_TYPE, Request.DEFAULT_CONTENT_TYPE);
        }
        conn.setDoOutput(true);
        conn.getOutputStream().write(content);
    }

}
