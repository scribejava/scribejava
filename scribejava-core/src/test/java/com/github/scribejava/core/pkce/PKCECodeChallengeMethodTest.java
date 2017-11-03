package com.github.scribejava.core.pkce;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * test PKCE according to<br/>
 * Appendix B. Example for the S256 code_challenge_method<br/>
 * https://tools.ietf.org/html/rfc7636#appendix-B
 */
public class PKCECodeChallengeMethodTest {

    private static final byte[] RANDOM_BYTES = new byte[]{116, 24, (byte) 223, (byte) 180, (byte) 151, (byte) 153,
        (byte) 224, 37, 79, (byte) 250, 96, 125, (byte) 216, (byte) 173, (byte) 187, (byte) 186, 22, (byte) 212, 37, 77,
        105, (byte) 214, (byte) 191, (byte) 240, 91, 88, 5, 88, 83, (byte) 132, (byte) 141, 121};

    @Test
    public void testGeneratingPKCE() {
        final PKCE pkce = new PKCEService().generatePKCE(RANDOM_BYTES);

        assertEquals(PKCECodeChallengeMethod.S256, pkce.getCodeChallengeMethod());
        assertEquals("dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk", pkce.getCodeVerifier());
        assertEquals("E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM", pkce.getCodeChallenge());
    }

}
