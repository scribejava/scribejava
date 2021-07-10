package com.github.scribejava.apis;

import com.github.scribejava.apis.microsoftazureactivedirectory.BaseMicrosoftAzureActiveDirectoryApi;
import com.github.scribejava.apis.microsoftazureactivedirectory.MicrosoftAzureActiveDirectory20BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;

/**
 * Microsoft Azure Active Directory Api v 2.0
 *
 * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-protocols-oauth-code">
 * Understand the OAuth 2.0 authorization code flow in Azure AD | Microsoft Docs</a>
 * @see <a href="https://developer.microsoft.com/en-us/graph/docs/concepts/v1-overview">
 * Microsoft Graph REST API v1.0 reference</a>
 * @see <a href="https://portal.azure.com">https://portal.azure.com</a>
 */
public class MicrosoftAzureActiveDirectory20Api extends BaseMicrosoftAzureActiveDirectoryApi {

    protected MicrosoftAzureActiveDirectory20Api() {
        this(COMMON_TENANT);
    }

    protected MicrosoftAzureActiveDirectory20Api(String tenant) {
        super(tenant);
    }

    private static class InstanceHolder {

        private static final MicrosoftAzureActiveDirectory20Api INSTANCE = new MicrosoftAzureActiveDirectory20Api();
    }

    public static MicrosoftAzureActiveDirectory20Api instance() {
        return InstanceHolder.INSTANCE;
    }

    public static MicrosoftAzureActiveDirectory20Api custom(String tenant) {
        return new MicrosoftAzureActiveDirectory20Api(tenant);
    }

    @Override
    public BearerSignature getBearerSignature() {
        return MicrosoftAzureActiveDirectory20BearerSignature.instance();
    }

    @Override
    protected String getEndpointVersionPath() {
        return "/v2.0";
    }
}
