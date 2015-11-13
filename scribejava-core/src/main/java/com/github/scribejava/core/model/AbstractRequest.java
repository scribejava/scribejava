package com.github.scribejava.core.model;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuthService;

/**
 * The representation of an OAuth HttpRequest.
 *
 * @author Pablo Fernandez
 */
public abstract class AbstractRequest {

    protected static final String CONTENT_LENGTH = "Content-Length";
    protected static final String CONTENT_TYPE = "Content-Type";
    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String OAUTH_PREFIX = "oauth_";

    private final String url;
    private final Verb verb;
    private final ParameterList querystringParams = new ParameterList();
    private final ParameterList bodyParams = new ParameterList();
    private final Map<String, String> headers = new HashMap<>();
    private boolean connectionKeepAlive;
    private boolean followRedirects = true;
    private OAuthService service;

    private String payload;
    private String charset;
    private byte[] bytePayload;
    private final Map<String, String> oauthParameters = new HashMap<>();

    private String realm;

    /**
     * Default constructor.
     *
     * @param verb Http verb/method
     * @param url resource URL
     * @param service OAuthService
     */
    public AbstractRequest(final Verb verb, final String url, final OAuthService service) {
        this.verb = verb;
        this.url = url;
        this.service = service;
    }

    /**
     * Adds an OAuth parameter.
     *
     * @param key name of the parameter
     * @param value value of the parameter
     * @throws IllegalArgumentException if the parameter is not an OAuth parameter
     */
    public void addOAuthParameter(final String key, final String value) {
        oauthParameters.put(checkKey(key), value);
    }

    private String checkKey(final String key) {
        if (key.startsWith(OAUTH_PREFIX) || key.equals(OAuthConstants.SCOPE) || key.equals(OAuthConstants.REALM)) {
            return key;
        } else {
            throw new IllegalArgumentException(
                    String.format("OAuth parameters must either be '%s', '%s' or start with '%s'", OAuthConstants.SCOPE, OAuthConstants.REALM, OAUTH_PREFIX));
        }
    }

    public Map<String, String> getOauthParameters() {
        return oauthParameters;
    }

    public void setRealm(final String realm) {
        this.realm = realm;
    }

    public String getRealm() {
        return realm;
    }

    /**
     * Returns the complete url (host + resource + encoded querystring parameters).
     *
     * @return the complete url.
     */
    public String getCompleteUrl() {
        return querystringParams.appendTo(url);
    }

    /**
     * Add an HTTP Header to the Request
     *
     * @param key the header name
     * @param value the header value
     */
    public void addHeader(final String key, final String value) {
        this.headers.put(key, value);
    }

    /**
     * Add a body Parameter (for POST/ PUT Requests)
     *
     * @param key the parameter name
     * @param value the parameter value
     */
    public void addBodyParameter(final String key, final String value) {
        this.bodyParams.add(key, value);
    }

    /**
     * Add a QueryString parameter
     *
     * @param key the parameter name
     * @param value the parameter value
     */
    public void addQuerystringParameter(final String key, final String value) {
        this.querystringParams.add(key, value);
    }

    public void addParameter(final String key, final String value) {
        if (hasBodyContent()) {
            bodyParams.add(key, value);
        } else {
            querystringParams.add(key, value);
        }
    }

    protected boolean hasBodyContent() {
        return verb == Verb.PUT || verb == Verb.POST;
    }

    /**
     * Add body payload. This method is used when the HTTP body is not a form-url-encoded string, but another thing. Like for example XML. Note: The
     * contents are not part of the OAuth signature
     *
     * @param payload the body of the request
     */
    public void addPayload(final String payload) {
        this.payload = payload;
    }

    /**
     * Overloaded version for byte arrays
     *
     * @param payload byte[]
     */
    public void addPayload(final byte[] payload) {
        this.bytePayload = payload.clone();
    }

    /**
     * Get a {@link ParameterList} with the query string parameters.
     *
     * @return a {@link ParameterList} containing the query string parameters.
     * @throws OAuthException if the request URL is not valid.
     */
    public ParameterList getQueryStringParams() {
        try {
            final ParameterList result = new ParameterList();
            final String queryString = new URL(url).getQuery();
            result.addQuerystring(queryString);
            result.addAll(querystringParams);
            return result;
        } catch (MalformedURLException mue) {
            throw new OAuthException("Malformed URL", mue);
        }
    }

    /**
     * Obtains a {@link ParameterList} of the body parameters.
     *
     * @return a {@link ParameterList}containing the body parameters.
     */
    public ParameterList getBodyParams() {
        return bodyParams;
    }

    /**
     * Obtains the URL of the HTTP Request.
     *
     * @return the original URL of the HTTP Request
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the URL without the port and the query string part.
     *
     * @return the OAuth-sanitized URL
     */
    public String getSanitizedUrl() {
        if (url.startsWith("http://") && (url.endsWith(":80") || url.contains(":80/"))) {
            return url.replaceAll("\\?.*", "").replaceAll(":80", "");
        } else if (url.startsWith("https://") && (url.endsWith(":443") || url.contains(":443/"))) {
            return url.replaceAll("\\?.*", "").replaceAll(":443", "");
        } else {
            return url.replaceAll("\\?.*", "");
        }
    }

    /**
     * Returns the body of the request
     *
     * @return form encoded string
     *
     * @throws OAuthException if the charset chosen is not supported
     */
    public String getBodyContents() {
        try {
            return new String(getByteBodyContents(), getCharset());
        } catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Unsupported Charset: " + charset, uee);
        }
    }

    byte[] getByteBodyContents() {
        if (bytePayload != null) {
            return bytePayload;
        }
        final String body = (payload == null) ? bodyParams.asFormUrlEncodedString() : payload;
        try {
            return body.getBytes(getCharset());
        } catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Unsupported Charset: " + getCharset(), uee);
        }
    }

    @Override
    public String toString() {
        return String.format("@Request(%s %s)", getVerb(), getUrl());
    }

    public Verb getVerb() {
        return verb;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getCharset() {
        return charset == null ? Charset.defaultCharset().name() : charset;
    }

    /**
     * Set the charset of the body of the request
     *
     * @param charsetName name of the charset of the request
     */
    public void setCharset(final String charsetName) {
        charset = charsetName;
    }

    /**
     * Sets whether the underlying Http Connection is persistent or not.
     *
     * @param connectionKeepAlive boolean
     *
     * @see <a
     * href="http://download.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html">http://download.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html</a>
     */
    public void setConnectionKeepAlive(final boolean connectionKeepAlive) {
        this.connectionKeepAlive = connectionKeepAlive;
    }

    /**
     * Sets whether the underlying Http Connection follows redirects or not.
     *
     * Defaults to true (follow redirects)
     *
     * @see <a
     * href="http://docs.oracle.com/javase/6/docs/api/java/net/HttpURLConnection.html#setInstanceFollowRedirects(boolean)">http://docs.oracle.com/javase/6/docs/api/java/net/HttpURLConnection.html#setInstanceFollowRedirects(boolean)</a>
     * @param followRedirects boolean
     */
    public void setFollowRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public boolean isConnectionKeepAlive() {
        return connectionKeepAlive;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public OAuthService getService() {
        return service;
    }
}
