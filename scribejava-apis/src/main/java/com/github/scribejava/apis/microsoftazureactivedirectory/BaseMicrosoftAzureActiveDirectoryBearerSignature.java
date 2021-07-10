package com.github.scribejava.apis.microsoftazureactivedirectory;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureAuthorizationRequestHeaderField;

public abstract class BaseMicrosoftAzureActiveDirectoryBearerSignature
        extends BearerSignatureAuthorizationRequestHeaderField {

    private final String acceptedFormat;

    protected BaseMicrosoftAzureActiveDirectoryBearerSignature(String acceptedFormat) {
        this.acceptedFormat = acceptedFormat;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
        request.addHeader("Accept", acceptedFormat);
    }
}
