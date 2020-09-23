package com.github.scribejava.core.model;

public class DeviceCode {
    private String deviceCode;
    private String userCode;
    private String verificationUri;
    private int intervalSeconds;
    private long expiresAtMillis;

    public DeviceCode(String deviceCode, String userCode, String verificationUri,
            int intervalSeconds, int expiresInSeconds) {
        this.deviceCode = deviceCode;
        this.userCode = userCode;
        this.verificationUri = verificationUri;
        this.intervalSeconds = intervalSeconds;
        expiresAtMillis = System.currentTimeMillis() + (expiresInSeconds * 1000);
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getVerificationUri() {
        return verificationUri;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public long getExpiresAtMillis() {
        return expiresAtMillis;
    }
}
