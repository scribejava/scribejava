package org.scribe.model;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.scribe.exceptions.OAuthException;

/**
 * Provides an implementation of Scribe Response class that wraps an 
 * Apache HttpResponse object (instead of the default URLConnection based
 * implementation)
 * 
 * @author mkishor
 */
public class HttpClientResponse extends Response {
    
    public HttpClientResponse(HttpResponse response) {
        this.code = response.getStatusLine().getStatusCode();
        for (HeaderIterator iter = response.headerIterator(); iter.hasNext(); ) {
            Header header = iter.nextHeader();
            headers.put(header.getName(), header.getValue());
        }
        try {
            this.stream = response.getEntity().getContent();
        } catch (Exception e) {
            throw new OAuthException("Error reading the response", e);
        }
    }
    
    // all the other methods are valid as-is - work off of the variables initialized
    // in the constructor...
    
}
