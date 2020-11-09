package com.github.scribejava.apis.google;

import com.github.scribejava.core.extractors.DeviceAuthorizationJsonExtractor;

public class GoogleDeviceAuthorizationJsonExtractor extends DeviceAuthorizationJsonExtractor {

    protected GoogleDeviceAuthorizationJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final GoogleDeviceAuthorizationJsonExtractor INSTANCE
                = new GoogleDeviceAuthorizationJsonExtractor();
    }

    public static GoogleDeviceAuthorizationJsonExtractor instance() {
        return GoogleDeviceAuthorizationJsonExtractor.InstanceHolder.INSTANCE;
    }

    @Override
    protected String getVerificationUriParamName() {
        return "verification_url";
    }

}
