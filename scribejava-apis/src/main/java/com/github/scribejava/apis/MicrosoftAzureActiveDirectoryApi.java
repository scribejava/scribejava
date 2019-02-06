package com.github.scribejava.apis;

import com.github.scribejava.apis.microsoftazureactivedirectory.BaseMicrosoftAzureActiveDirectoryApi;
import com.github.scribejava.apis.microsoftazureactivedirectory.MicrosoftAzureActiveDirectoryBearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;

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

    private final String resource;

    protected MicrosoftAzureActiveDirectoryApi() {
        this(COMMON_TENANT, null);
    }

    protected MicrosoftAzureActiveDirectoryApi(String tenant, String resource) {
        super(tenant);
        this.resource = resource;
    }

    private static class InstanceHolder {

        private static final MicrosoftAzureActiveDirectoryApi INSTANCE = new MicrosoftAzureActiveDirectoryApi();
    }

    public static MicrosoftAzureActiveDirectoryApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static MicrosoftAzureActiveDirectoryApi customTenant(String tenant) {
        return new MicrosoftAzureActiveDirectoryApi(tenant, null);
    }

    public static MicrosoftAzureActiveDirectoryApi customResource(String resource) {
        return new MicrosoftAzureActiveDirectoryApi(COMMON_TENANT, resource);
    }

    public static MicrosoftAzureActiveDirectoryApi custom(String tenant, String resource) {
        return new MicrosoftAzureActiveDirectoryApi(tenant, resource);
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        final String authorizationBaseUrl = super.getAuthorizationBaseUrl();
        return resource == null || resource.isEmpty() ? authorizationBaseUrl
                : authorizationBaseUrl + "?resource=" + resource;
    }

    @Override
    public BearerSignature getBearerSignature() {
        return MicrosoftAzureActiveDirectoryBearerSignature.instance();
    }
}
