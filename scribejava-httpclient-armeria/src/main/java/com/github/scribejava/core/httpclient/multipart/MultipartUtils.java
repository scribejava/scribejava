package com.github.scribejava.core.httpclient.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class MultipartUtils {

    // copied from com.github.scribejava.core.httpclient.jdk.JDKHttpClient#getPayload
    public static ByteArrayOutputStream getPayload(MultipartPayload multipartPayload) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        final String preamble = multipartPayload.getPreamble();
        if (preamble != null) {
            os.write(preamble.getBytes());
        }
        final List<BodyPartPayload> bodyParts = multipartPayload.getBodyParts();
        if (!bodyParts.isEmpty()) {
            final String boundary = multipartPayload.getBoundary();
            final byte[] startBoundary = ("\r\n--" + boundary + "\r\n").getBytes();

            for (BodyPartPayload bodyPart : bodyParts) {
                os.write(startBoundary);

                final Map<String, String> bodyPartHeaders = bodyPart.getHeaders();
                if (bodyPartHeaders != null) {
                    for (Map.Entry<String, String> header : bodyPartHeaders.entrySet()) {
                        os.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes());
                    }
                }

                if (bodyPart instanceof MultipartPayload) {
                    getPayload((MultipartPayload) bodyPart).writeTo(os);
                } else if (bodyPart instanceof ByteArrayBodyPartPayload) {
                    os.write("\r\n".getBytes());
                    os.write(((ByteArrayBodyPartPayload) bodyPart).getPayload());
                } else {
                    throw new AssertionError(bodyPart.getClass());
                }
            }

            os.write(("\r\n--" + boundary + "--\r\n").getBytes());
            final String epilogue = multipartPayload.getEpilogue();
            if (epilogue != null) {
                os.write((epilogue + "\r\n").getBytes());
            }

        }
        return os;
    }

}
