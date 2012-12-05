package org.scribe.model;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.*;

/**
 * Date: 28/11/2012
 * Time: 17:10
 *
 * @author Antonis Thodis
 */
public class HttpsOAuthRequest extends OAuthRequest {

    private boolean createNewSSLSocketFactory;
    private String keyStoreFilePath;

    private static SSLSocketFactory sslFactory;

    /**
     * Default constructor.
     *
     * @param verb Http verb/method
     * @param url  resource URL
     * @param keyStoreFilePath path to keystore file containing the trusted certificate needed in order to establish the ssl connection.
     * @param createNewSSLSocketFactory flag indicating if a new SSLSocketFactory should be created for this request.
     *                                  If set to false, a new SSLSocketFactory will be created on the first HttpsOAuthRequest created and all subsequent
     *                                  requests will use the same SSLSocketFactory. If set to true, a new SSLSocketFactory will always be created for every
     *                                  new HttpsOAuthRequest.
     */
    public HttpsOAuthRequest(Verb verb, String url, String keyStoreFilePath, boolean createNewSSLSocketFactory) {
        super(verb, url);
        this.keyStoreFilePath = keyStoreFilePath;
        this.createNewSSLSocketFactory = createNewSSLSocketFactory;
    }

    @Override
    void createConnection() throws IOException {
        String completeUrl = getCompleteUrl();
        if (getConnection() == null) {
            System.setProperty("http.keepAlive", isConnectionKeepAlive() ? "true" : "false");
            HttpsURLConnection connection = (HttpsURLConnection) new URL(completeUrl).openConnection();

            if (sslFactory == null || createNewSSLSocketFactory) {
                sslFactory = createSSLSocketFactory();
            }

            connection.setSSLSocketFactory(sslFactory);

            setConnection(connection);
        }
    }

    private SSLSocketFactory createSSLSocketFactory() throws IOException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(keyStoreFilePath);
                keyStore.load(fis, null);
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tmf.getTrustManagers(), null);
            return ctx.getSocketFactory();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } 
    }

}
