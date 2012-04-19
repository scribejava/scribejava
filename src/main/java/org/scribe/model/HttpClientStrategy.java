package org.scribe.model;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.scribe.exceptions.OAuthException;

/**
 * This class allows us to use the HttpClient library to make the HTTP calls,
 * and the scribe library to provide an OAuth abstraction over it.
 * 
 * @author mkishor
 */
public class HttpClientStrategy implements HttpStrategy {
    private HttpClient httpClient;

    public HttpClientStrategy(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Response send(Request request) {
        try {
            String completeUrl = request.getCompleteUrl();
            String verb = request.getVerb().name();
            HttpUriRequest httpRequest = newHttpUriRequest(verb, completeUrl);
            addHeaders(request, httpRequest);
            if ("POST".equals(verb) || "PUT".equals(verb)) {
                ByteArrayEntity entity = new ByteArrayEntity(request.getByteBodyContents());
                ((HttpEntityEnclosingRequest) httpRequest).setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            return new HttpClientResponse(httpResponse);
        } catch (Exception e) {
            throw new OAuthException("Error making the request", e);
        }
    }

    private void addHeaders(Request request, HttpUriRequest httpRequest) {
        for (String key : request.getHeaders().keySet()) {
            httpRequest.addHeader(key, request.getHeaders().get(key));
        }
    }
    
    protected HttpUriRequest newHttpUriRequest(String verb, String uri) {
        if ("GET".equals(verb)) {
            return new HttpGet(uri);
        } else if ("PUT".equals(verb)) {
            return new HttpPut(uri);
        } else if ("DELETE".equals(verb)) {
            return new HttpDelete(uri);
        } else {
            return new HttpPost(uri);
        }
    }

}
