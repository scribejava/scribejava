package com.github.scribejava.core.pkce;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to hold code_challenge, code_challenge_method and code_verifier for https://tools.ietf.org/html/rfc7636
 */
public class PKCE {

    public static final String PKCE_CODE_CHALLENGE_METHOD_PARAM = "code_challenge_method";
    public static final String PKCE_CODE_CHALLENGE_PARAM = "code_challenge";
    public static final String PKCE_CODE_VERIFIER_PARAM = "code_verifier";

    private String codeChallenge;
    private PKCECodeChallengeMethod codeChallengeMethod = PKCECodeChallengeMethod.S256;
    private String codeVerifier;

    public String getCodeChallenge() {
        return codeChallenge;
    }

    public void setCodeChallenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
    }

    public PKCECodeChallengeMethod getCodeChallengeMethod() {
        return codeChallengeMethod;
    }

    public void setCodeChallengeMethod(PKCECodeChallengeMethod codeChallengeMethod) {
        this.codeChallengeMethod = codeChallengeMethod;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

    public Map<String, String> getAuthorizationUrlParams() {
        final Map<String, String> params = new HashMap<>();
        params.put(PKCE_CODE_CHALLENGE_PARAM, codeChallenge);
        params.put(PKCE_CODE_CHALLENGE_METHOD_PARAM, codeChallengeMethod.name());
        return params;
    }
}
