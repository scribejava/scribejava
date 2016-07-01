package com.github.scribejava.apis;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import com.github.scribejava.apis.salesforce.SalesforceJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

/**
 * This class is an implementation of the Salesforce OAuth2 API.
 */
public class SalesforceApi extends DefaultApi20 {

    private String hostName = "";
    private static final String HOST_PRODUCTION = "login.salesforce.com";
    private static final String HOST_SANDBOX = "test.salesforce.com";
    private static final String PROTOCOL = "https://";
    private static final String ACCESS_URL = "/services/oauth2/token";
    private static final String AUTHORIZE_URL = "/services/oauth2/authorize";

    /**
     * The default implementation connects to the Salesforce production
     * environment.
     * 
     * If you want to connect to a Sandbox environment you've to call the
     * {@link #withSandbox} method after object creation.
     */
    protected SalesforceApi() {
        hostName = HOST_PRODUCTION;
        try {
            final SSLSocket socket = (SSLSocket) SSLContext.getDefault().getSocketFactory().createSocket();
            if (!isTLSv11orUpperEnabled(socket)) {
                throw new IllegalStateException("Salesforce API required to use TLSv1.1 or upper. "
                        + "Enabled it by invoking method initTLSv11orUpper or somehow else");
            }
        } catch (NoSuchAlgorithmException | IOException ex) {
            throw new IllegalStateException("Salesforce API required to use TLSv1.1 or upper. "
                    + "Enabled it by invoking method initTLSv11orUpper or somehow else");
        }
    }

    private static class InstanceHolder {
        private static final SalesforceApi INSTANCE = new SalesforceApi();
    }

    public static SalesforceApi instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Calling this method sets the target hostname to login.salesforce.com.
     * 
     * It is only needed to call this method when you've switched to a Sandbox
     * environment before.
     */
    public void withProduction() {
        hostName = HOST_PRODUCTION;
    }

    /**
     * Calling this method sets the target hostname to test.salesforce.com.
     */
    public void withSandbox() {
        hostName = HOST_SANDBOX;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return PROTOCOL + hostName + ACCESS_URL;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return PROTOCOL + hostName + AUTHORIZE_URL;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return SalesforceJsonTokenExtractor.instance();
    }

    private static boolean isTLSv11orUpperEnabled(final SSLSocket socket) {
        for (String protocol : socket.getEnabledProtocols()) {
            if ("TLSv1.2".equals(protocol) || "TLSv1.1".equals(protocol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Salesforce API requires to use TLSv1.1 or upper.
     * <p>
     * Java 8 have TLS 1.2 enabled by default. java 7 - no, you should invoke
     * this method or turn TLS&gt;=1.1 somehow else
     * </p>
     *
     * @throws java.security.NoSuchAlgorithmException
     *             in case your jvm doesn't support TLSv1.1 and TLSv1.2
     * @throws java.security.KeyManagementException
     * @throws java.io.IOException
     */
    public static void initTLSv11orUpper() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        final SSLSocket socket = (SSLSocket) SSLContext.getDefault().getSocketFactory().createSocket();
        if (isTLSv11orUpperEnabled(socket)) {
            return;
        }
        boolean supportTLSv11 = false;
        boolean supportTLSv12 = false;
        for (String protocol : socket.getSupportedProtocols()) {
            if ("TLSv1.2".equals(protocol)) {
                supportTLSv12 = true;
                break;
            }
            if ("TLSv1.1".equals(protocol)) {
                supportTLSv11 = true;
            }
        }

        final SSLContext context;
        if (supportTLSv12) {
            context = SSLContext.getInstance("TLSv1.2");
        } else if (supportTLSv11) {
            context = SSLContext.getInstance("TLSv1.1");
        } else {
            throw new NoSuchAlgorithmException("for Salesforce API to work you need jvm with TLS 1.1 or 1.2 support");
        }

        context.init(null, null, null);
        SSLContext.setDefault(context);
    }
}
