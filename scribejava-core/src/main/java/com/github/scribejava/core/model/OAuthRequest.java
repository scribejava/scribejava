package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.httpclient.multipart.FileByteArrayBodyPartPayload;
import com.github.scribejava.core.httpclient.multipart.MultipartPayload;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The representation of an OAuth HttpRequest.
 */
public class OAuthRequest {

    private static final String OAUTH_PREFIX = "oauth_";

    private final String url;
    private final Verb verb;
    private final ParameterList querystringParams = new ParameterList();
    private final ParameterList bodyParams = new ParameterList();
    private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private String charset;

    private String stringPayload;
    private byte[] byteArrayPayload;
    private File filePayload;
    private MultipartPayload multipartPayload;

    private final Map<String, String> oauthParameters = new HashMap<>();

    private String realm;

    /**
     * Default constructor.
     *
     * @param verb Http verb/method
     * @param url resource URL
     */
    public OAuthRequest(Verb verb, String url) {
        this.verb = verb;
        this.url = url;
    }

    /**
     * Adds an OAuth parameter.
     *
     * @param key name of the parameter
     * @param value value of the parameter
     * @throws IllegalArgumentException if the parameter is not an OAuth parameter
     */
    public void addOAuthParameter(String key, String value) {
        oauthParameters.put(checkKey(key), value);
    }

    private String checkKey(String key) {
        if (key.startsWith(OAUTH_PREFIX) || key.equals(OAuthConstants.SCOPE) || key.equals(OAuthConstants.REALM)) {
            return key;
        } else {
            throw new IllegalArgumentException(
                    String.format("OAuth parameters must either be '%s', '%s' or start with '%s'", OAuthConstants.SCOPE,
                            OAuthConstants.REALM, OAUTH_PREFIX));
        }
    }

    public Map<String, String> getOauthParameters() {
        return oauthParameters;
    }

    public void setRealm(String realm) {
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
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Add a body Parameter (for POST/ PUT Requests)
     *
     * @param key the parameter name
     * @param value the parameter value
     */
    public void addBodyParameter(String key, String value) {
        bodyParams.add(key, value);
    }

    /**
     * Add a QueryString parameter
     *
     * @param key the parameter name
     * @param value the parameter value
     */
    public void addQuerystringParameter(String key, String value) {
        querystringParams.add(key, value);
    }

    public void addParameter(String key, String value) {
        if (verb.isPermitBody()) {
            bodyParams.add(key, value);
        } else {
            querystringParams.add(key, value);
        }
    }

    public MultipartPayload getMultipartPayload() {
        return multipartPayload;
    }

    public void setMultipartPayload(MultipartPayload multipartPayload) {
        this.multipartPayload = multipartPayload;
    }

    public void initMultipartPayload() {
        this.multipartPayload = new MultipartPayload();
    }

    public void initMultipartPayload(String boundary) {
        this.multipartPayload = new MultipartPayload(boundary);
    }

    public void initMultipartPayload(String subtype, String boundary) {
        this.multipartPayload = new MultipartPayload(subtype, boundary);
    }

    public void initMultipartPayload(Map<String, String> headers) {
        this.multipartPayload = new MultipartPayload(headers);
    }

    public void initMultipartPayload(String boundary, Map<String, String> headers) {
        this.multipartPayload = new MultipartPayload(boundary, headers);
    }

    public void initMultipartPayload(String subtype, String boundary, Map<String, String> headers) {
        this.multipartPayload = new MultipartPayload(subtype, boundary, headers);
    }

    public void setByteArrayBodyPartPayloadInMultipartPayload(byte[] bodyPartPayload) {
        initMultipartPayload();
        addByteArrayBodyPartPayloadInMultipartPayload(bodyPartPayload);
    }

    public void setByteArrayBodyPartPayloadInMultipartPayload(byte[] bodyPartPayload, String contentType) {
        initMultipartPayload();
        addByteArrayBodyPartPayloadInMultipartPayload(bodyPartPayload, contentType);
    }

    public void setByteArrayBodyPartPayloadInMultipartPayload(byte[] bodyPartPayload, Map<String, String> headers) {
        initMultipartPayload();
        addByteArrayBodyPartPayloadInMultipartPayload(bodyPartPayload, headers);
    }

    public void addByteArrayBodyPartPayloadInMultipartPayload(byte[] bodyPartPayload) {
        multipartPayload.addBodyPart(bodyPartPayload);
    }

    public void addByteArrayBodyPartPayloadInMultipartPayload(byte[] bodyPartPayload, String contentType) {
        multipartPayload.addBodyPart(bodyPartPayload, contentType);
    }

    public void addByteArrayBodyPartPayloadInMultipartPayload(byte[] bodyPartPayload, Map<String, String> headers) {
        multipartPayload.addBodyPart(bodyPartPayload, headers);
    }

    public void setFileByteArrayBodyPartPayloadInMultipartPayload(byte[] fileContent) {
        initMultipartPayload();
        addFileByteArrayBodyPartPayloadInMultipartPayload(fileContent);
    }

    public void setFileByteArrayBodyPartPayloadInMultipartPayload(String contentType, byte[] fileContent) {
        initMultipartPayload();
        addFileByteArrayBodyPartPayloadInMultipartPayload(contentType, fileContent);
    }

    public void setFileByteArrayBodyPartPayloadInMultipartPayload(byte[] fileContent, String name) {
        initMultipartPayload();
        addFileByteArrayBodyPartPayloadInMultipartPayload(fileContent, name);
    }

    public void setFileByteArrayBodyPartPayloadInMultipartPayload(String contentType, byte[] fileContent, String name) {
        initMultipartPayload();
        addFileByteArrayBodyPartPayloadInMultipartPayload(contentType, fileContent, name);
    }

    public void setFileByteArrayBodyPartPayloadInMultipartPayload(byte[] fileContent, String name, String filename) {
        initMultipartPayload();
        addFileByteArrayBodyPartPayloadInMultipartPayload(fileContent, name, filename);
    }

    public void setFileByteArrayBodyPartPayloadInMultipartPayload(String contentType, byte[] fileContent, String name,
            String filename) {
        initMultipartPayload();
        addFileByteArrayBodyPartPayloadInMultipartPayload(contentType, fileContent, name, filename);
    }

    public void setFileByteArrayBodyPartPayloadInMultipartPayload(
            FileByteArrayBodyPartPayload fileByteArrayBodyPartPayload) {
        initMultipartPayload();
        addFileByteArrayBodyPartPayloadInMultipartPayload(fileByteArrayBodyPartPayload);
    }

    public void addFileByteArrayBodyPartPayloadInMultipartPayload(byte[] fileContent) {
        multipartPayload.addFileBodyPart(fileContent);
    }

    public void addFileByteArrayBodyPartPayloadInMultipartPayload(String contentType, byte[] fileContent) {
        multipartPayload.addFileBodyPart(contentType, fileContent);
    }

    public void addFileByteArrayBodyPartPayloadInMultipartPayload(byte[] fileContent, String name) {
        multipartPayload.addFileBodyPart(fileContent, name);
    }

    public void addFileByteArrayBodyPartPayloadInMultipartPayload(String contentType, byte[] fileContent, String name) {
        multipartPayload.addFileBodyPart(contentType, fileContent, name);
    }

    public void addFileByteArrayBodyPartPayloadInMultipartPayload(byte[] fileContent, String name, String filename) {
        multipartPayload.addFileBodyPart(fileContent, name, filename);
    }

    public void addFileByteArrayBodyPartPayloadInMultipartPayload(String contentType, byte[] fileContent, String name,
            String filename) {
        multipartPayload.addFileBodyPart(contentType, fileContent, name, filename);
    }

    public void addFileByteArrayBodyPartPayloadInMultipartPayload(
            FileByteArrayBodyPartPayload fileByteArrayBodyPartPayload) {
        multipartPayload.addBodyPart(fileByteArrayBodyPartPayload);
    }

    /**
     * Set body payload. This method is used when the HTTP body is not a form-url-encoded string, but another thing.
     * Like for example XML. Note: The contents are not part of the OAuth signature
     *
     * @param payload the body of the request
     */
    public void setPayload(String payload) {
        resetPayload();
        stringPayload = payload;
    }

    /**
     * Overloaded version for byte arrays
     *
     * @param payload byte[]
     */
    public void setPayload(byte[] payload) {
        resetPayload();
        byteArrayPayload = payload.clone();
    }

    /**
     * Overloaded version for File
     *
     * @param payload File
     */
    public void setPayload(File payload) {
        resetPayload();
        filePayload = payload;
    }

    private void resetPayload() {
        stringPayload = null;
        byteArrayPayload = null;
        filePayload = null;
        multipartPayload = null;
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
     * Returns the body of the request (set in {@link #setPayload(java.lang.String)})
     *
     * @return form encoded string
     */
    public String getStringPayload() {
        return stringPayload;
    }

    /**
     * @return the body of the request (set in {@link #setPayload(byte[])} or in
     * {@link #addBodyParameter(java.lang.String, java.lang.String)} )
     */
    public byte[] getByteArrayPayload() {
        if (byteArrayPayload != null) {
            return byteArrayPayload;
        }
        final String body = bodyParams.asFormUrlEncodedString();
        try {
            return body.getBytes(getCharset());
        } catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Unsupported Charset: " + getCharset(), uee);
        }
    }

    public File getFilePayload() {
        return filePayload;
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
    public void setCharset(String charsetName) {
        charset = charsetName;
    }

    public interface ResponseConverter<T> {

        T convert(Response response) throws IOException;
    }

}
