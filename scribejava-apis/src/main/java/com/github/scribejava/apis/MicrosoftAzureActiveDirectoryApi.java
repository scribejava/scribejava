package com.github.scribejava.apis;

import com.github.scribejava.apis.microsoftazureactivedirectory.BaseMicrosoftAzureActiveDirectoryApi;

/**
 * Microsoft Azure Active Directory Api
 *
 * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-protocols-oauth-code">
 * Understand the OAuth 2.0 authorization code flow in Azure AD | Microsoft Docs</a>
 * @see <a
 * href="https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-devquickstarts-webapp-java">
 * Azure AD Java web app Getting Started | Microsoft Docs</a>
 * @see <a href="https://msdn.microsoft.com/en-us/library/azure/ad/graph/api/signed-in-user-operations">
 * Azure AD Graph API Operations on the Signed-in User</a>
 * @see <a href="https://portal.azure.com">https://portal.azure.com</a>
 */
public class MicrosoftAzureActiveDirectoryApi extends BaseMicrosoftAzureActiveDirectoryApi {

    protected MicrosoftAzureActiveDirectoryApi() {
        super(MicrosoftAzureActiveDirectoryVersion.V_1_0);
    }

    private static class InstanceHolder {

        private static final MicrosoftAzureActiveDirectoryApi INSTANCE = new MicrosoftAzureActiveDirectoryApi();
    }

    public static MicrosoftAzureActiveDirectoryApi instance() {
        return InstanceHolder.INSTANCE;
    }
}
