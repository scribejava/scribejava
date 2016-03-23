package com.github.scribejava.core.model;

import java.util.Objects;

/**
 * Defines the configuration for an OpenID connect provider. This class is compatible with
 * <a href="http://openid.net/specs/openid-connect-discovery-1_0.html">the OpenID connect discovery specification</a>.
 * <p>
 * This class provides the configuration as a simple POJO. The user of this class is responsible for making sure the
 * right values are filled in, preferably using some JSON library that directly parses the discovery result.
 * </p>
 */
public class OpenIdConnectConfig {
    private String issuer;

    private String authorizationEndpoint;

    private String tokenEndpoint;

    private String userinfoEndpoint;

    private String jwksUri;

    private String registrationEndpoint;

    private String[] scopesSupported;

    private String[] responseTypesSupported;

    private String[] responseModesSupported;

    private String[] grantTypesSupported;

    private String[] acrValuesSupported;

    private String[] subjectTypesSupported;

    private String[] idTokenSigningAlgValuesSupported;

    private String[] idTokenEncryptionAlgValuesSupported;

    private String[] idTokenEncryptionEncValuesSupported;

    private String[] userinfoSigningAlgValuesSupported;

    private String[] userinfoEncryptionAlgValuesSupported;

    private String[] userinfoEncryptionEncValuesSupported;

    private String[] requestObjectSigningAlgValuesSupported;

    private String[] requestObjectEncryptionAlgValuesSupported;

    private String[] requestObjectEncryptionEncValuesSupported;

    private String[] tokenEndpointAuthMethodsSupported;

    private String[] tokenEndpointAuthSigningAlgValuesSupported;

    private String[] displayValuesSupported;

    private String[] claimTypesSupported;

    private String[] claimsSupported;

    private String serviceDocumentation;

    private String[] claimsLocalesSupported;

    private String[] uiLocalesSupported;

    private boolean claimsParameterSupported;

    private boolean requestParameterSupported;

    private boolean requestUriParameterSupported;

    private boolean requireRequestUriRegistration;

    private String opPolicyUri;

    private String opTosUri;

    private String checkSessionIframe;

    private String endSessionEndpoint;

    public OpenIdConnectConfig() {
        claimsParameterSupported = false;
        requestParameterSupported = false;
        requestUriParameterSupported = true;
        requireRequestUriRegistration = false;
    }

    /**
     * @return <code>true</code> if this configuration is valid, which means that all required properties of the core
     *         discovery specification are properly filled.
     */
    public boolean isValid() {
        return isValidEntry(issuer) && isValidEntry(authorizationEndpoint) && isValidEntry(jwksUri)
                && isValidEntry(responseTypesSupported) && isValidEntry(subjectTypesSupported) &&
                isValidEntry(idTokenSigningAlgValuesSupported);
    }

    private static boolean isValidEntry(String... values) {
        return values != null && values.length > 0 && values[0] != null;
    }

    /**
     * @see OpenIdConnectConstants#ISSUER
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @see OpenIdConnectConstants#ISSUER
     */
    public void setIssuer(String issuer) {
        this.issuer = Objects.requireNonNull(issuer);
    }

    /**
     * @see OpenIdConnectConstants#AUTHORIZATION_ENDPOINT
     */
    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#AUTHORIZATION_ENDPOINT
     */
    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = Objects.requireNonNull(authorizationEndpoint);
    }

    /**
     * @see OpenIdConnectConstants#TOKEN_ENDPOINT
     */
    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#TOKEN_ENDPOINT
     */
    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_ENDPOINT
     */
    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_ENDPOINT
     */
    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#JWKS_URI
     */
    public String getJwksUri() {
        return jwksUri;
    }

    /**
     * @see OpenIdConnectConstants#JWKS_URI
     */
    public void setJwksUri(String jwksUri) {
        this.jwksUri = Objects.requireNonNull(jwksUri);
    }

    /**
     * @see OpenIdConnectConstants#REGISTRATION_ENDPOINT
     */
    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#REGISTRATION_ENDPOINT
     */
    public void setRegistrationEndpoint(String registrationEndpoint) {
        this.registrationEndpoint = registrationEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#SCOPES_SUPPORTED
     */
    public String[] getScopesSupported() {
        return scopesSupported;
    }

    /**
     * @see OpenIdConnectConstants#SCOPES_SUPPORTED
     */
    public void setScopesSupported(String[] scopesSupported) {
        this.scopesSupported = scopesSupported;
    }

    /**
     * @see OpenIdConnectConstants#RESPONSE_TYPES_SUPPORTED
     */
    public String[] getResponseTypesSupported() {
        return responseTypesSupported;
    }

    /**
     * @see OpenIdConnectConstants#RESPONSE_TYPES_SUPPORTED
     */
    public void setResponseTypesSupported(String... responseTypesSupported) {
        this.responseTypesSupported = Objects.requireNonNull(responseTypesSupported);
    }

    /**
     * @see OpenIdConnectConstants#RESPONSE_MODES_SUPPORTED
     */
    public String[] getResponseModesSupported() {
        return responseModesSupported;
    }

    /**
     * @see OpenIdConnectConstants#RESPONSE_MODES_SUPPORTED
     */
    public void setResponseModesSupported(String... responseModesSupported) {
        this.responseModesSupported = responseModesSupported;
    }

    /**
     * @see OpenIdConnectConstants#GRANT_TYPES_SUPPORTED
     */
    public String[] getGrantTypesSupported() {
        return grantTypesSupported;
    }

    /**
     * @see OpenIdConnectConstants#GRANT_TYPES_SUPPORTED
     */
    public void setGrantTypesSupported(String... grantTypesSupported) {
        this.grantTypesSupported = grantTypesSupported;
    }

    /**
     * @see OpenIdConnectConstants#ACR_VALUES_SUPPORTED
     */
    public String[] getAcrValuesSupported() {
        return acrValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#ACR_VALUES_SUPPORTED
     */
    public void setAcrValuesSupported(String... acrValuesSupported) {
        this.acrValuesSupported = acrValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#SUBJECT_TYPES_SUPPORTED
     */
    public String[] getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    /**
     * @see OpenIdConnectConstants#SUBJECT_TYPES_SUPPORTED
     */
    public void setSubjectTypesSupported(String... subjectTypesSupported) {
        this.subjectTypesSupported = Objects.requireNonNull(subjectTypesSupported);
    }

    /**
     * @see OpenIdConnectConstants#ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED
     */
    public String[] getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED
     */
    public void setIdTokenSigningAlgValuesSupported(String... idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = Objects.requireNonNull(idTokenSigningAlgValuesSupported);
    }

    /**
     * @see OpenIdConnectConstants#ID_TOKEN_ENCRYPTION_ALG_VALUES_SUPPORTED
     */
    public String[] getIdTokenEncryptionAlgValuesSupported() {
        return idTokenEncryptionAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#ID_TOKEN_ENCRYPTION_ALG_VALUES_SUPPORTED
     */
    public void setIdTokenEncryptionAlgValuesSupported(String... idTokenEncryptionAlgValuesSupported) {
        this.idTokenEncryptionAlgValuesSupported = idTokenEncryptionAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#ID_TOKEN_ENCRYPTION_ENC_VALUES_SUPPORTED
     */
    public String[] getIdTokenEncryptionEncValuesSupported() {
        return idTokenEncryptionEncValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#ID_TOKEN_ENCRYPTION_ENC_VALUES_SUPPORTED
     */
    public void setIdTokenEncryptionEncValuesSupported(String... idTokenEncryptionEncValuesSupported) {
        this.idTokenEncryptionEncValuesSupported = idTokenEncryptionEncValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_SIGNING_ALG_VALUES_SUPPORTED
     */
    public String[] getUserinfoSigningAlgValuesSupported() {
        return userinfoSigningAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_SIGNING_ALG_VALUES_SUPPORTED
     */
    public void setUserinfoSigningAlgValuesSupported(String... userinfoSigningAlgValuesSupported) {
        this.userinfoSigningAlgValuesSupported = userinfoSigningAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_ENCRYPTION_ALG_VALUES_SUPPORTED
     */
    public String[] getUserinfoEncryptionAlgValuesSupported() {
        return userinfoEncryptionAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_ENCRYPTION_ALG_VALUES_SUPPORTED
     */
    public void setUserinfoEncryptionAlgValuesSupported(String... userinfoEncryptionAlgValuesSupported) {
        this.userinfoEncryptionAlgValuesSupported = userinfoEncryptionAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_ENCRYPTION_ENC_VALUES_SUPPORTED
     */
    public String[] getUserinfoEncryptionEncValuesSupported() {
        return userinfoEncryptionEncValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#USERINFO_ENCRYPTION_ENC_VALUES_SUPPORTED
     */
    public void setUserinfoEncryptionEncValuesSupported(String... userinfoEncryptionEncValuesSupported) {
        this.userinfoEncryptionEncValuesSupported = userinfoEncryptionEncValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_OBJECT_SIGNING_ALG_VALUES_SUPPORTED
     */
    public String[] getRequestObjectSigningAlgValuesSupported() {
        return requestObjectSigningAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_OBJECT_SIGNING_ALG_VALUES_SUPPORTED
     */
    public void setRequestObjectSigningAlgValuesSupported(String... requestObjectSigningAlgValuesSupported) {
        this.requestObjectSigningAlgValuesSupported = requestObjectSigningAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_OBJECT_ENCRYPTION_ALG_VALUES_SUPPORTED
     */
    public String[] getRequestObjectEncryptionAlgValuesSupported() {
        return requestObjectEncryptionAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_OBJECT_ENCRYPTION_ALG_VALUES_SUPPORTED
     */
    public void setRequestObjectEncryptionAlgValuesSupported(String... requestObjectEncryptionAlgValuesSupported) {
        this.requestObjectEncryptionAlgValuesSupported = requestObjectEncryptionAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_OBJECT_ENCRYPTION_ENC_VALUES_SUPPORTED
     */
    public String[] getRequestObjectEncryptionEncValuesSupported() {
        return requestObjectEncryptionEncValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_OBJECT_ENCRYPTION_ENC_VALUES_SUPPORTED
     */
    public void setRequestObjectEncryptionEncValuesSupported(String... requestObjectEncryptionEncValuesSupported) {
        this.requestObjectEncryptionEncValuesSupported = requestObjectEncryptionEncValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED
     */
    public String[] getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }

    /**
     * @see OpenIdConnectConstants#TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED
     */
    public void setTokenEndpointAuthMethodsSupported(String... tokenEndpointAuthMethodsSupported) {
        this.tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
    }

    /**
     * @see OpenIdConnectConstants#TOKEN_ENDPOINT_AUTH_SIGNING_ALG_VALUES_SUPPORTED
     */
    public String[] getTokenEndpointAuthSigningAlgValuesSupported() {
        return tokenEndpointAuthSigningAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#TOKEN_ENDPOINT_AUTH_SIGNING_ALG_VALUES_SUPPORTED
     */
    public void setTokenEndpointAuthSigningAlgValuesSupported(String... tokenEndpointAuthSigningAlgValuesSupported) {
        this.tokenEndpointAuthSigningAlgValuesSupported = tokenEndpointAuthSigningAlgValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#DISPLAY_VALUES_SUPPORTED
     */
    public String[] getDisplayValuesSupported() {
        return displayValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#DISPLAY_VALUES_SUPPORTED
     */
    public void setDisplayValuesSupported(String... displayValuesSupported) {
        this.displayValuesSupported = displayValuesSupported;
    }

    /**
     * @see OpenIdConnectConstants#CLAIM_TYPES_SUPPORTED
     */
    public String[] getClaimTypesSupported() {
        return claimTypesSupported;
    }

    /**
     * @see OpenIdConnectConstants#CLAIM_TYPES_SUPPORTED
     */
    public void setClaimTypesSupported(String... claimTypesSupported) {
        this.claimTypesSupported = claimTypesSupported;
    }

    /**
     * @see OpenIdConnectConstants#CLAIMS_SUPPORTED
     */
    public String[] getClaimsSupported() {
        return claimsSupported;
    }

    /**
     * @see OpenIdConnectConstants#CLAIMS_SUPPORTED
     */
    public void setClaimsSupported(String... claimsSupported) {
        this.claimsSupported = claimsSupported;
    }

    /**
     * @see OpenIdConnectConstants#SERVICE_DOCUMENTATION
     */
    public String getServiceDocumentation() {
        return serviceDocumentation;
    }

    /**
     * @see OpenIdConnectConstants#SERVICE_DOCUMENTATION
     */
    public void setServiceDocumentation(String serviceDocumentation) {
        this.serviceDocumentation = serviceDocumentation;
    }

    /**
     * @see OpenIdConnectConstants#CLAIMS_LOCALES_SUPPORTED
     */
    public String[] getClaimsLocalesSupported() {
        return claimsLocalesSupported;
    }

    /**
     * @see OpenIdConnectConstants#CLAIMS_LOCALES_SUPPORTED
     */
    public void setClaimsLocalesSupported(String... claimsLocalesSupported) {
        this.claimsLocalesSupported = claimsLocalesSupported;
    }

    /**
     * @see OpenIdConnectConstants#UI_LOCALES_SUPPORTED
     */
    public String[] getUiLocalesSupported() {
        return uiLocalesSupported;
    }

    /**
     * @see OpenIdConnectConstants#UI_LOCALES_SUPPORTED
     */
    public void setUiLocalesSupported(String... uiLocalesSupported) {
        this.uiLocalesSupported = uiLocalesSupported;
    }

    /**
     * @see OpenIdConnectConstants#CLAIMS_PARAMETER_SUPPORTED
     */
    public boolean isClaimsParameterSupported() {
        return claimsParameterSupported;
    }

    /**
     * @see OpenIdConnectConstants#CLAIMS_PARAMETER_SUPPORTED
     */
    public void setClaimsParameterSupported(boolean claimsParameterSupported) {
        this.claimsParameterSupported = claimsParameterSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_PARAMETER_SUPPORTED
     */
    public boolean isRequestParameterSupported() {
        return requestParameterSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_PARAMETER_SUPPORTED
     */
    public void setRequestParameterSupported(boolean requestParameterSupported) {
        this.requestParameterSupported = requestParameterSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_URI_PARAMETER_SUPPORTED
     */
    public boolean isRequestUriParameterSupported() {
        return requestUriParameterSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUEST_URI_PARAMETER_SUPPORTED
     */
    public void setRequestUriParameterSupported(boolean requestUriParameterSupported) {
        this.requestUriParameterSupported = requestUriParameterSupported;
    }

    /**
     * @see OpenIdConnectConstants#REQUIRE_REQUEST_URI_REGISTRATION
     */
    public boolean isRequireRequestUriRegistration() {
        return requireRequestUriRegistration;
    }

    /**
     * @see OpenIdConnectConstants#REQUIRE_REQUEST_URI_REGISTRATION
     */
    public void setRequireRequestUriRegistration(boolean requireRequestUriRegistration) {
        this.requireRequestUriRegistration = requireRequestUriRegistration;
    }

    /**
     * @see OpenIdConnectConstants#OP_POLICY_URI
     */
    public String getOpPolicyUri() {
        return opPolicyUri;
    }

    /**
     * @see OpenIdConnectConstants#OP_POLICY_URI
     */
    public void setOpPolicyUri(String opPolicyUri) {
        this.opPolicyUri = opPolicyUri;
    }

    /**
     * @see OpenIdConnectConstants#OP_TOS_URI
     */
    public String getOpTosUri() {
        return opTosUri;
    }

    /**
     * @see OpenIdConnectConstants#OP_TOS_URI
     */
    public void setOpTosUri(String opTosUri) {
        this.opTosUri = opTosUri;
    }

    /**
     * @see OpenIdConnectConstants#CHECK_SESSION_IFRAME
     */
    public String getCheckSessionIframe() {
        return checkSessionIframe;
    }

    /**
     * @see OpenIdConnectConstants#CHECK_SESSION_IFRAME
     */
    public void setCheckSessionIframe(String checkSessionIframe) {
        this.checkSessionIframe = Objects.requireNonNull(checkSessionIframe);
    }

    /**
     * @see OpenIdConnectConstants#END_SESSION_ENDPOINT
     */
    public String getEndSessionEndpoint() {
        return endSessionEndpoint;
    }

    /**
     * @see OpenIdConnectConstants#END_SESSION_ENDPOINT
     */
    public void setEndSessionEndpoint(String endSessionEndpoint) {
        this.endSessionEndpoint = Objects.requireNonNull(endSessionEndpoint);
    }
}
