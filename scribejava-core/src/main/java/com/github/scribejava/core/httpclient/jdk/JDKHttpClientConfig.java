package com.github.scribejava.core.httpclient.jdk;

import com.github.scribejava.core.httpclient.HttpClientConfig;

public class JDKHttpClientConfig implements HttpClientConfig {

    private Integer connectTimeout;
    private Integer readTimeout;

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
}
