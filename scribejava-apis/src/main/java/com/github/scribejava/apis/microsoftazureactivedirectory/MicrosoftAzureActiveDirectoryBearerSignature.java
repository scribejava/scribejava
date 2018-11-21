package com.github.scribejava.apis.microsoftazureactivedirectory;

public class MicrosoftAzureActiveDirectoryBearerSignature extends BaseMicrosoftAzureActiveDirectoryBearerSignature {

    protected MicrosoftAzureActiveDirectoryBearerSignature() {
        super("application/json; odata=minimalmetadata; streaming=true; charset=utf-8");
    }

    private static class InstanceHolder {

        private static final MicrosoftAzureActiveDirectoryBearerSignature INSTANCE
                = new MicrosoftAzureActiveDirectoryBearerSignature();
    }

    public static MicrosoftAzureActiveDirectoryBearerSignature instance() {
        return InstanceHolder.INSTANCE;
    }
}
