package org.scribe.model;

import java.io.IOException;
import java.net.HttpURLConnection;

public class URLConnectionStrategyStub extends URLConnectionStrategy {
    private ConnectionStub connection;
    
    public URLConnectionStrategyStub(ConnectionStub connection) {
        this.connection = connection;
    }

    @Override
    protected HttpURLConnection createConnection(Request request) throws IOException {
        try {
            return connection;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
