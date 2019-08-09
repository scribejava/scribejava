package com.github.scribejava.core.httpclient.multipart;

import org.hamcrest.core.StringStartsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MultipartPayloadTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValidCheckBoundarySyntax() {
        MultipartPayload.checkBoundarySyntax("0aA'()+_,-./:=?");
        MultipartPayload.checkBoundarySyntax("0aA'()+_,- ./:=?");
        MultipartPayload.checkBoundarySyntax(" 0aA'()+_,-./:=?");
        MultipartPayload.checkBoundarySyntax("1234567890123456789012345678901234567890123456789012345678901234567890");
    }

    @Test
    public void testNonValidLastWhiteSpaceCheckBoundarySyntax() {
        final String boundary = "0aA'()+_,-./:=? ";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(
                StringStartsWith.startsWith("{'boundary'='" + boundary + "'} has invaid syntax. Should be '"));
        MultipartPayload.checkBoundarySyntax(boundary);
    }

    @Test
    public void testNonValidEmptyCheckBoundarySyntax() {
        final String boundary = "";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(
                StringStartsWith.startsWith("{'boundary'='" + boundary + "'} has invaid syntax. Should be '"));
        MultipartPayload.checkBoundarySyntax(boundary);
    }

    @Test
    public void testNonValidIllegalSymbolCheckBoundarySyntax() {
        final String boundary = "0aA'()+_;,-./:=? ";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(
                StringStartsWith.startsWith("{'boundary'='" + boundary + "'} has invaid syntax. Should be '"));
        MultipartPayload.checkBoundarySyntax(boundary);
    }

    @Test
    public void testNonValidTooLongCheckBoundarySyntax() {
        final String boundary = "12345678901234567890123456789012345678901234567890123456789012345678901";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(
                StringStartsWith.startsWith("{'boundary'='" + boundary + "'} has invaid syntax. Should be '"));
        MultipartPayload.checkBoundarySyntax(boundary);
    }

    @Test
    public void testNonValidNullCheckBoundarySyntax() {
        final String boundary = null;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(
                StringStartsWith.startsWith("{'boundary'='" + boundary + "'} has invaid syntax. Should be '"));
        MultipartPayload.checkBoundarySyntax(boundary);
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
}
