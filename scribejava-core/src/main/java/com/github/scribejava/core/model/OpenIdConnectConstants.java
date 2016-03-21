package com.github.scribejava.core.model;

/**
 * Defines several constants that are used by in the OpenID connect configuration.
 */
public interface OpenIdConnectConstants {
    /**
     * REQUIRED. URL using the https scheme with no query or fragment component that the OP asserts as its Issuer
     * Identifier. If Issuer discovery is supported (see Section 2), this value MUST be identical to the issuer value
     * returned by WebFinger. This also MUST be identical to the iss Claim value in ID Tokens issued from this Issuer.
     */
    String KEY_ISSUER = "issuer";

    /**
     * REQUIRED. URL of the OP's OAuth 2.0 Authorization Endpoint.
     */
    String KEY_AUTHORIZATION_ENDPOINT = "authorization_endpoint";

    /**
     * URL of the OP's OAuth 2.0 Token Endpoint. This is REQUIRED unless only the Implicit Flow is used.
     */
    String KEY_TOKEN_ENDPOINT = "token_endpoint";

    /**
     * RECOMMENDED. URL of the OP's UserInfo Endpoint. This URL MUST use the https scheme and MAY contain port, path,
     * and query parameter components.
     */
    String KEY_USERINFO_ENDPOINT = "userinfo_endpoint";

    /**
     * REQUIRED. URL of the OP's JSON Web Key Set document. This contains the signing key(s) the RP uses to validate
     * signatures from the OP. The JWK Set MAY also contain the Server's encryption key(s), which are used by RPs to
     * encrypt requests to the Server. When both signing and encryption keys are made available, a use (Key Use)
     * parameter value is REQUIRED for all keys in the referenced JWK Set to indicate each key's intended usage.
     * Although some algorithms allow the same key to be used for both signatures and encryption, doing so is NOT
     * RECOMMENDED, as it is less secure. The JWK x5c parameter MAY be used to provide X.509 representations of keys
     * provided. When used, the bare key values MUST still be present and MUST match those in the certificate.
     */
    String KEY_JWKS_URI = "jwks_uri";

    /**
     * RECOMMENDED. URL of the OP's Dynamic Client Registration Endpoint.
     */
    String KEY_REGISTRATION_ENDPOINT = "registration_endpoint";

    /**
     * RECOMMENDED. JSON array containing a list of the OAuth 2.0 scope values that this server supports. The server
     * MUST support the openid scope value. Servers MAY choose not to advertise some supported scope values even when
     * this parameter is used, although those defined in SHOULD be listed, if supported.
     */
    String KEY_SCOPES_SUPPORTED = "scopes_supported";

    /**
     * REQUIRED. JSON array containing a list of the OAuth 2.0 response_type values that this OP supports. Dynamic
     * OpenID Providers MUST support the code, id_token, and the token id_token Response Type values.
     */
    String KEY_RESPONSE_TYPES_SUPPORTED = "response_types_supported";

    /**
     * OPTIONAL. JSON array containing a list of the OAuth 2.0 response_mode values that this OP supports, as specified
     * in OAuth 2.0 Multiple Response Type Encoding Practices. If omitted, the default for Dynamic OpenID Providers is
     * ["query", "fragment"].
     */
    String KEY_RESPONSE_MODES_SUPPORTED = "response_modes_supported";

    /**
     * OPTIONAL. JSON array containing a list of the OAuth 2.0 Grant Type values that this OP supports. Dynamic OpenID
     * Providers MUST support the authorization_code and implicit Grant Type values and MAY support other Grant Types.
     * If omitted, the default value is ["authorization_code", "implicit"].
     */
    String KEY_GRANT_TYPES_SUPPORTED = "grant_types_supported";

    /**
     * OPTIONAL. JSON array containing a list of the Authentication Context Class References that this OP supports.
     */
    String KEY_ACR_VALUES_SUPPORTED = "acr_values_supported";

    /**
     * REQUIRED. JSON array containing a list of the Subject Identifier types that this OP supports. Valid types include
     * pairwise and public.
     */
    String KEY_SUBJECT_TYPES_SUPPORTED = "subject_types_supported";

    /**
     * REQUIRED. JSON array containing a list of the JWS signing algorithms (alg values) supported by the OP for the ID
     * Token to encode the Claims in a JWT. The algorithm RS256 MUST be included. The value none MAY be supported, but
     * MUST NOT be used unless the Response Type used returns no ID Token from the Authorization Endpoint (such as when
     * using the Authorization Code Flow).
     */
    String KEY_ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED = "id_token_signing_alg_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWE encryption algorithms (alg values) supported by the OP for the
     * ID Token to encode the Claims in a JWT.
     */
    String KEY_ID_TOKEN_ENCRYPTION_ALG_VALUES_SUPPORTED = "id_token_encryption_alg_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWE encryption algorithms (enc values) supported by the OP for the
     * ID Token to encode the Claims in a JWT.
     */
    String KEY_ID_TOKEN_ENCRYPTION_ENC_VALUES_SUPPORTED = "id_token_encryption_enc_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWS signing algorithms (alg values) supported by the UserInfo
     * Endpoint to encode the Claims in a JWT. The value none MAY be included.
     */
    String KEY_USERINFO_SIGNING_ALG_VALUES_SUPPORTED = "userinfo_signing_alg_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWE encryption algorithms (alg values) supported by the UserInfo
     * Endpoint to encode the Claims in a JWT.
     */
    String KEY_USERINFO_ENCRYPTION_ALG_VALUES_SUPPORTED = "userinfo_encryption_alg_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWE encryption algorithms (enc values) supported by the UserInfo
     * Endpoint to encode the Claims in a JWT.
     */
    String KEY_USERINFO_ENCRYPTION_ENC_VALUES_SUPPORTED = "userinfo_encryption_enc_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWS signing algorithms (alg values) supported by the OP for Request
     * Objects, which are described in Section 6.1 of OpenID Connect Core 1.0. These algorithms are used both when the
     * Request Object is passed by value (using the request parameter) and when it is passed by reference (using the
     * request_uri parameter). Servers SHOULD support none and RS256.
     */
    String KEY_REQUEST_OBJECT_SIGNING_ALG_VALUES_SUPPORTED = "request_object_signing_alg_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWE encryption algorithms (alg values) supported by the OP for
     * Request Objects. These algorithms are used both when the Request Object is passed by value and when it is passed
     * by reference.
     */
    String KEY_REQUEST_OBJECT_ENCRYPTION_ALG_VALUES_SUPPORTED = "request_object_encryption_alg_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWE encryption algorithms (enc values) supported by the OP for
     * Request Objects. These algorithms are used both when the Request Object is passed by value and when it is passed
     * by reference.
     */
    String KEY_REQUEST_OBJECT_ENCRYPTION_ENC_VALUES_SUPPORTED = "request_object_encryption_enc_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of Client Authentication methods supported by this Token Endpoint. The
     * options are client_secret_post, client_secret_basic, client_secret_jwt, and private_key_jwt, as described in
     * Section 9 of OpenID Connect Core 1.0. Other authentication methods MAY be defined by extensions. If omitted, the
     * default is client_secret_basic -- the HTTP Basic Authentication Scheme specified in Section 2.3.1 of OAuth 2.0.
     */
    String KEY_TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED = "token_endpoint_auth_methods_supported";

    /**
     * OPTIONAL. JSON array containing a list of the JWS signing algorithms (alg values) supported by the Token Endpoint
     * for the signature on the JWT used to authenticate the Client at the Token Endpoint for the private_key_jwt and
     * client_secret_jwt authentication methods. Servers SHOULD support RS256. The value none MUST NOT be used.
     */
    String KEY_TOKEN_ENDPOINT_AUTH_SIGNING_ALG_VALUES_SUPPORTED = "token_endpoint_auth_signing_alg_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the display parameter values that the OpenID Provider supports. These
     * values are described in Section 3.1.2.1 of OpenID Connect Core 1.0.
     */
    String KEY_DISPLAY_VALUES_SUPPORTED = "display_values_supported";

    /**
     * OPTIONAL. JSON array containing a list of the Claim Types that the OpenID Provider supports. These Claim Types
     * are described in Section 5.6 of OpenID Connect Core 1.0. Values defined by this specification are normal,
     * aggregated, and distributed. If omitted, the implementation supports only normal Claims.
     */
    String KEY_CLAIM_TYPES_SUPPORTED = "claim_types_supported";

    /**
     * RECOMMENDED. JSON array containing a list of the Claim Names of the Claims that the OpenID Provider MAY be able
     * to supply values for. Note that for privacy or other reasons, this might not be an exhaustive list.
     */
    String KEY_CLAIMS_SUPPORTED = "claims_supported";

    /**
     * OPTIONAL. URL of a page containing human-readable information that developers might want or need to know when
     * using the OpenID Provider. In particular, if the OpenID Provider does not support Dynamic Client Registration,
     * then information on how to register Clients needs to be provided in this documentation.
     */
    String KEY_SERVICE_DOCUMENTATION = "service_documentation";

    /**
     * OPTIONAL. Languages and scripts supported for values in Claims being returned, represented as a JSON array of
     * BCP47 language tag values. Not all languages and scripts are necessarily supported for all Claim values.
     */
    String KEY_CLAIMS_LOCALES_SUPPORTED = "claims_locales_supported";

    /**
     * OPTIONAL. Languages and scripts supported for the user interface, represented as a JSON array of BCP47 language
     * tag values.
     */
    String KEY_UI_LOCALES_SUPPORTED = "ui_locales_supported";

    /**
     * OPTIONAL. Boolean value specifying whether the OP supports use of the claims parameter, with true indicating
     * support. If omitted, the default value is false.
     */
    String KEY_CLAIMS_PARAMETER_SUPPORTED = "claims_parameter_supported";

    /**
     * OPTIONAL. Boolean value specifying whether the OP supports use of the request parameter, with true indicating
     * support. If omitted, the default value is false.
     */
    String KEY_REQUEST_PARAMETER_SUPPORTED = "request_parameter_supported";

    /**
     * OPTIONAL. Boolean value specifying whether the OP supports use of the request_uri parameter, with true indicating
     * support. If omitted, the default value is true.
     */
    String KEY_REQUEST_URI_PARAMETER_SUPPORTED = "request_uri_parameter_supported";

    /**
     * OPTIONAL. Boolean value specifying whether the OP requires any request_uri values used to be pre-registered using
     * the request_uris registration parameter. Pre-registration is REQUIRED when the value is true. If omitted, the
     * default value is false.
     */
    String KEY_REQUIRE_REQUEST_URI_REGISTRATION = "require_request_uri_registration";

    /**
     * OPTIONAL. URL that the OpenID Provider provides to the person registering the Client to read about the OP's
     * requirements on how the Relying Party can use the data provided by the OP. The registration process SHOULD
     * display this URL to the person registering the Client if it is given.
     */
    String KEY_OP_POLICY_URI = "op_policy_uri";

    /**
     * OPTIONAL. URL that the OpenID Provider provides to the person registering the Client to read about OpenID
     * Provider's terms of service. The registration process SHOULD display this URL to the person registering the
     * Client if it is given.
     */
    String KEY_OP_TOS_URI = "op_tos_uri";

    /**
     * REQUIRED. URL of an OP iframe that supports cross-origin communications for session state information with the RP
     * Client, using the HTML5 postMessage API. The page is loaded from an invisible iframe embedded in an RP page so
     * that it can run in the OP's security context. It accepts postMessage requests from the relevant RP iframe and
     * uses postMessage to post back the login status of the End-User at the OP.
     */
    String KEY_CHECK_SESSION_IFRAME = "check_session_iframe";

    /**
     * REQUIRED. URL at the OP to which an RP can perform a redirect to request that the End-User be logged out at the
     * OP.
     */
    String KEY_END_SESSION_ENDPOINT = "end_session_endpoint";

}
