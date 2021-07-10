package com.github.scribejava.apis.tutby;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;

public class TutByBearerSignature implements BearerSignature {

    protected TutByBearerSignature() {
    }

    private static class InstanceHolder {

        private static final TutByBearerSignature INSTANCE = new TutByBearerSignature();
    }

    public static TutByBearerSignature instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        request.addQuerystringParameter(OAuthConstants.TOKEN, accessToken);
    }
}
