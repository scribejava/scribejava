package com.github.scribejava.core.httpclient.jdk;

import java.net.Proxy;

import com.github.scribejava.core.httpclient.HttpClientConfig;

public class JDKHttpClientConfig implements HttpClientConfig {

    private Integer connectTimeout;
    private Integer readTimeout;
    private boolean followRedirects = true;
    private Proxy proxy;

    @Override
    public JDKHttpClientConfig createDefaultConfig() {
        return defaultConfig();
    }

    public static JDKHttpClientConfig defaultConfig() {
        return new JDKHttpClientConfig();
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public JDKHttpClientConfig withConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public JDKHttpClientConfig withReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public JDKHttpClientConfig withProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
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
    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    /**
     * Sets whether the underlying Http Connection follows redirects or not.
     *
     * Defaults to true (follow redirects)
     *
     * @see <a
     * href="http://docs.oracle.com/javase/6/docs/api/java/net/HttpURLConnection.html#setInstanceFollowRedirects(boolean)">http://docs.oracle.com/javase/6/docs/api/java/net/HttpURLConnection.html#setInstanceFollowRedirects(boolean)</a>
     * @param followRedirects boolean
     * @return this for chaining methods invocations
     */
    public JDKHttpClientConfig withFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }
}
