package com.github.scribejava.core.httpclient.multipart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class MultipartPayloadTest {

    @Test
    public void testValidCheckBoundarySyntax() {
        MultipartPayload.checkBoundarySyntax("0aA'()+_,-./:=?");
        MultipartPayload.checkBoundarySyntax("0aA'()+_,- ./:=?");
        MultipartPayload.checkBoundarySyntax(" 0aA'()+_,-./:=?");
        MultipartPayload.checkBoundarySyntax("1234567890123456789012345678901234567890123456789012345678901234567890");
    }

    @Test
    public void testNonValidLastWhiteSpaceCheckBoundarySyntax() {
        testBoundary("0aA'()+_,-./:=? ");
    }

    @Test
    public void testNonValidEmptyCheckBoundarySyntax() {
        testBoundary("");
    }

    @Test
    public void testNonValidIllegalSymbolCheckBoundarySyntax() {
        testBoundary("0aA'()+_;,-./:=? ");
    }

    @Test
    public void testNonValidTooLongCheckBoundarySyntax() {
        testBoundary("12345678901234567890123456789012345678901234567890123456789012345678901");
    }

    @Test
    public void testNonValidNullCheckBoundarySyntax() {
        testBoundary(null);
    }

    @Test
    public void testParseBoundaryFromHeader() {
        assertNull(MultipartPayload.parseBoundaryFromHeader(null));

        assertEquals("0aA'()+_,-./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=\"0aA'()+_,-./:=?\""));

        assertEquals("0aA'()+_, -./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=\"0aA'()+_, -./:=?\""));

        assertEquals("0aA'()+_, -./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=\"0aA'()+_, -./:=? \""));

        assertEquals("0aA'()+_,-./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=0aA'()+_,-./:=?"));

        assertEquals("0aA'()+_, -./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=0aA'()+_, -./:=?"));

        assertEquals("0aA'()+_, -./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=0aA'()+_, -./:=? "));

        assertEquals(" 0aA'()+_, -./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary= 0aA'()+_, -./:=?"));

        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundar=0aA'()+_, -./:=? "));
        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype; "));
        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype;"));
        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype"));
        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary="));

        assertEquals("0aA'()+_,",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=0aA'()+_,; -./:=? "));

        assertEquals("0aA'()+_, -./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=\"0aA'()+_, -./:=?"));

        assertEquals("0aA'()+_, -./:=?",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=0aA'()+_, -./:=?\""));

        assertEquals("1234567890123456789012345678901234567890123456789012345678901234567890",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; "
                        + "boundary=1234567890123456789012345678901234567890123456789012345678901234567890"));

        assertEquals("1234567890123456789012345678901234567890123456789012345678901234567890",
                MultipartPayload.parseBoundaryFromHeader("multipart/subtype; "
                        + "boundary=12345678901234567890123456789012345678901234567890123456789012345678901"));

        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary="));
        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=\"\""));
        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=;123"));
        assertNull(MultipartPayload.parseBoundaryFromHeader("multipart/subtype; boundary=\"\"123"));
    }

    private static void testBoundary(final String boundary) {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                MultipartPayload.checkBoundarySyntax(boundary);
            }
        });
        assertTrue(thrown.getMessage().startsWith("{'boundary'='" + boundary + "'} has invalid syntax. Should be '"));
    }
}
