package com.github.scribejava.apis.microsoftazureactivedirectory;

public class MicrosoftAzureActiveDirectory20BearerSignature extends BaseMicrosoftAzureActiveDirectoryBearerSignature {

    protected MicrosoftAzureActiveDirectory20BearerSignature() {
        super("application/json");
    }

    private static class InstanceHolder {

        private static final MicrosoftAzureActiveDirectory20BearerSignature INSTANCE
                = new MicrosoftAzureActiveDirectory20BearerSignature();
    }

    public static MicrosoftAzureActiveDirectory20BearerSignature instance() {
        return InstanceHolder.INSTANCE;
    }
}
