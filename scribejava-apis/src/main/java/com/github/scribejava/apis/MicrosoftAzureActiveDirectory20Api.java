package com.github.scribejava.apis;

import com.github.scribejava.apis.microsoftazureactivedirectory.BaseMicrosoftAzureActiveDirectoryApi;

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
        super(MicrosoftAzureActiveDirectoryVersion.V_2_0);
    }

    private static class InstanceHolder {

        private static final MicrosoftAzureActiveDirectory20Api INSTANCE = new MicrosoftAzureActiveDirectory20Api();
    }

    public static MicrosoftAzureActiveDirectory20Api instance() {
        return InstanceHolder.INSTANCE;
    }
}
