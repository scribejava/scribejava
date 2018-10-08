package com.github.scribejava.apis.microsoftazureactivedirectory;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureAuthorizationRequestHeaderField;

public class MicrosoftAzureActiveDirectoryBearerSignature extends BearerSignatureAuthorizationRequestHeaderField {
    private static final String ACCEPTED_FORMAT
            = "application/json; odata=minimalmetadata; streaming=true; charset=utf-8";

    protected MicrosoftAzureActiveDirectoryBearerSignature() {
    }

    private static class InstanceHolder {

        private static final MicrosoftAzureActiveDirectoryBearerSignature INSTANCE
                = new MicrosoftAzureActiveDirectoryBearerSignature();
    }

    public static MicrosoftAzureActiveDirectoryBearerSignature instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
        request.addHeader("Accept", ACCEPTED_FORMAT);
    }
}
