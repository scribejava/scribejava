package com.github.scribejava.core.httpclient.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultipartUtils {

    private static final String B_CHARS_NO_SPACE_PATTERN = "0-9a-zA-Z'()+_,-./:=?";
    private static final String B_CHARS_PATTERN = B_CHARS_NO_SPACE_PATTERN + " ";
    private static final String BOUNDARY_PATTERN = '[' + B_CHARS_PATTERN + "]{0,69}[" + B_CHARS_NO_SPACE_PATTERN + ']';
    private static final Pattern BOUNDARY_REGEXP = Pattern.compile(BOUNDARY_PATTERN);
    private static final Pattern BOUNDARY_FROM_HEADER_REGEXP
            = Pattern.compile("; boundary=\"?(" + BOUNDARY_PATTERN + ")\"?");

    private MultipartUtils() {
    }

    public static void checkBoundarySyntax(String boundary) {
        if (boundary == null || !BOUNDARY_REGEXP.matcher(boundary).matches()) {
            throw new IllegalArgumentException("{'boundary'='" + boundary + "'} has invalid syntax. Should be '"
                    + BOUNDARY_PATTERN + "'.");
        }
    }

    public static String parseBoundaryFromHeader(String contentTypeHeader) {
        if (contentTypeHeader == null) {
            return null;
        }
        final Matcher matcher = BOUNDARY_FROM_HEADER_REGEXP.matcher(contentTypeHeader);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static String generateDefaultBoundary() {
        return "----ScribeJava----" + System.currentTimeMillis();
    }

    public static ByteArrayOutputStream getPayload(MultipartPayload multipartPayload) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        final String preamble = multipartPayload.getPreamble();
        if (preamble != null) {
            os.write((preamble + "\r\n").getBytes());
        }
        final List<BodyPartPayload> bodyParts = multipartPayload.getBodyParts();
        if (!bodyParts.isEmpty()) {
            final String boundary = multipartPayload.getBoundary();
            final byte[] startBoundary = ("--" + boundary + "\r\n").getBytes();

            for (BodyPartPayload bodyPart : bodyParts) {
                os.write(startBoundary);

                final Map<String, String> bodyPartHeaders = bodyPart.getHeaders();
                if (bodyPartHeaders != null) {
                    for (Map.Entry<String, String> header : bodyPartHeaders.entrySet()) {
                        os.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes());
                    }
                }

                os.write("\r\n".getBytes());
                if (bodyPart instanceof MultipartPayload) {
                    getPayload((MultipartPayload) bodyPart).writeTo(os);
                } else if (bodyPart instanceof ByteArrayBodyPartPayload) {
                    final ByteArrayBodyPartPayload byteArrayBodyPart = (ByteArrayBodyPartPayload) bodyPart;
                    os.write(byteArrayBodyPart.getPayload(), byteArrayBodyPart.getOff(), byteArrayBodyPart.getLen());
                } else {
                    throw new AssertionError(bodyPart.getClass());
                }
                os.write("\r\n".getBytes()); //CRLF for the next (starting or closing) boundary
            }

            os.write(("--" + boundary + "--").getBytes());
            final String epilogue = multipartPayload.getEpilogue();
            if (epilogue != null) {
                os.write(("\r\n" + epilogue).getBytes());
            }

        }
        return os;
    }
}
