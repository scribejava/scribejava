package com.github.scribejava.core.httpclient.jdk;

import com.github.scribejava.core.httpclient.HttpClientConfig;

public class JDKHttpClientConfig implements HttpClientConfig {

    private Integer connectTimeout;
    private Integer readTimeout;
    private boolean followRedirects = true;

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

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
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
}
