package com.github.scribejava.core.httpclient.jdk;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.multipart.ByteArrayBodyPartPayload;
import com.github.scribejava.core.httpclient.multipart.FileByteArrayBodyPartPayload;
import com.github.scribejava.core.httpclient.multipart.MultipartPayload;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class JDKHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new JDKHttpClient();
    }

    @Test
    public void testEmptyMultipartPayload() throws IOException {
        final MultipartPayload mP = new MultipartPayload();

        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> header : mP.getHeaders().entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }

        sb.append("\r\n").append(JDKHttpClient.getPayload(mP).toString());
        Assert.assertEquals("Content-Type: multipart/form-data; boundary=\"" + mP.getBoundary() + "\"\r\n\r\n",
                sb.toString());
    }

    @Test
    public void testSimpleMultipartPayload() throws IOException {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put("X-Header", "X-Value");
        headers.put("Content-Disposition", "Content-Disposition-Value");
        final MultipartPayload mP = new MultipartPayload("mixed", "simple boundary", headers);
        mP.setPreamble("This is the preamble.  It is to be ignored, though it\n"
                + "is a handy place for composition agents to include an\n"
                + "explanatory note to non-MIME conformant readers.");

        mP.addBodyPart(("This is implicitly typed plain US-ASCII text.\n"
                + "It does NOT end with a linebreak.").getBytes());

        final ByteArrayBodyPartPayload bP = new ByteArrayBodyPartPayload(
                ("This is explicitly typed plain US-ASCII text.\n"
                        + "It DOES end with a linebreak.\n").getBytes(),
                Collections.singletonMap(HttpClient.CONTENT_TYPE, "text/plain; charset=us-ascii"));
        mP.addBodyPart(bP);

        mP.setEpilogue("This is the epilogue.  It is also to be ignored.");

        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> header : mP.getHeaders().entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }
        sb.append("\r\n").append(JDKHttpClient.getPayload(mP).toString());
        Assert.assertEquals("X-Header: X-Value\r\n"
                + "Content-Disposition: Content-Disposition-Value\r\n"
                + "Content-Type: multipart/mixed; boundary=\"simple boundary\"\r\n"
                + "\r\n"
                + "This is the preamble.  It is to be ignored, though it\n"
                + "is a handy place for composition agents to include an\n"
                + "explanatory note to non-MIME conformant readers."
                + "\r\n"
                + "--simple boundary\r\n"
                + "\r\n"
                + "This is implicitly typed plain US-ASCII text.\n"
                + "It does NOT end with a linebreak."
                + "\r\n"
                + "--simple boundary\r\n"
                + "Content-Type: text/plain; charset=us-ascii\r\n"
                + "\r\n"
                + "This is explicitly typed plain US-ASCII text.\n"
                + "It DOES end with a linebreak.\n"
                + "\r\n"
                + "--simple boundary--\r\n"
                + "This is the epilogue.  It is also to be ignored.\r\n",
                sb.toString());
    }

    @Test
    public void testCRLFMultipartPayload() throws IOException {
        final MultipartPayload mP = new MultipartPayload("simple-boundary");
        mP.addBodyPart("It does NOT end with a linebreak.".getBytes());
        mP.addBodyPart("It does end with a \\r linebreak.\r".getBytes());
        mP.addBodyPart("It does end with a \\n linebreak.\n".getBytes());
        mP.addBodyPart("It does end with a \\r\\n linebreak.\r\n".getBytes());
        mP.addBodyPart("the last one".getBytes());

        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> header : mP.getHeaders().entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }
        sb.append("\r\n").append(JDKHttpClient.getPayload(mP).toString());
        Assert.assertEquals("Content-Type: multipart/form-data; boundary=\"simple-boundary\"\r\n"
                + "\r\n"
                + "\r\n"
                + "--simple-boundary\r\n"
                + "\r\n"
                + "It does NOT end with a linebreak."
                + "\r\n"
                + "--simple-boundary\r\n"
                + "\r\n"
                + "It does end with a \\r linebreak.\r"
                + "\r\n"
                + "--simple-boundary\r\n"
                + "\r\n"
                + "It does end with a \\n linebreak.\n"
                + "\r\n"
                + "--simple-boundary\r\n"
                + "\r\n"
                + "It does end with a \\r\\n linebreak.\r\n"
                + "\r\n"
                + "--simple-boundary\r\n"
                + "\r\n"
                + "the last one"
                + "\r\n"
                + "--simple-boundary--\r\n",
                sb.toString());
    }

    @Test
    public void testFileByteArrayBodyPartPayloadMultipartPayload() throws IOException {
        final MultipartPayload mP = new MultipartPayload("testFileByteArrayBodyPartPayloadMultipartPayload boundary");
        mP.addBodyPart(new FileByteArrayBodyPartPayload("fileContent".getBytes(), "name", "filename.ext"));

        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> header : mP.getHeaders().entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }

        sb.append("\r\n").append(JDKHttpClient.getPayload(mP).toString());
        Assert.assertEquals("Content-Type: multipart/form-data; "
                + "boundary=\"testFileByteArrayBodyPartPayloadMultipartPayload boundary\"\r\n"
                + "\r\n"
                + "\r\n"
                + "--testFileByteArrayBodyPartPayloadMultipartPayload boundary\r\n"
                + "Content-Disposition: form-data; name=\"name\"; filename=\"filename.ext\"\r\n"
                + "\r\n"
                + "fileContent"
                + "\r\n"
                + "--testFileByteArrayBodyPartPayloadMultipartPayload boundary--\r\n",
                sb.toString()
        );
    }

    @Test
    public void testComplexMultipartPayload() throws IOException {
        final MultipartPayload mP = new MultipartPayload("mixed", "unique-boundary-1");

        mP.setPreamble("This is the preamble area of a multipart message.\n"
                + "Mail readers that understand multipart format\n"
                + "should ignore this preamble.\n"
                + "\n"
                + "If you are reading this text, you might want to\n"
                + "consider changing to a mail reader that understands\n"
                + "how to properly display multipart messages.\n");

        mP.addBodyPart("... Some text appears here ...".getBytes());

        mP.addBodyPart(("This could have been part of the previous part, but\n"
                + "illustrates explicit versus implicit typing of body\n"
                + "parts.\n").getBytes(), "text/plain; charset=US-ASCII");

        final MultipartPayload innerMP = new MultipartPayload("parallel", "unique-boundary-2");
        mP.addBodyPart(innerMP);

        final Map<String, String> audioHeaders = new LinkedHashMap<>();
        audioHeaders.put("Content-Type", "audio/basic");
        audioHeaders.put("Content-Transfer-Encoding", "base64");
        innerMP.addBodyPart(("... base64-encoded 8000 Hz single-channel\n"
                + "    mu-law-format audio data goes here ...").getBytes(), audioHeaders);

        final Map<String, String> imageHeaders = new LinkedHashMap<>();
        imageHeaders.put("Content-Type", "image/jpeg");
        imageHeaders.put("Content-Transfer-Encoding", "base64");
        innerMP.addBodyPart("... base64-encoded image data goes here ...".getBytes(), imageHeaders);

        mP.addBodyPart(("This is <bold><italic>enriched.</italic></bold>\n"
                + "<smaller>as defined in RFC 1896</smaller>\n"
                + "\n"
                + "Isn't it\n"
                + "<bigger><bigger>cool?</bigger></bigger>\n").getBytes(), "text/enriched");

        mP.addBodyPart(("From: (mailbox in US-ASCII)\n"
                + "To: (address in US-ASCII)\n"
                + "Subject: (subject in US-ASCII)\n"
                + "Content-Type: Text/plain; charset=ISO-8859-1\n"
                + "Content-Transfer-Encoding: Quoted-printable\n"
                + "\n"
                + "... Additional text in ISO-8859-1 goes here ...\n").getBytes(), "message/rfc822");

        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> header : mP.getHeaders().entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }
        sb.append("\r\n").append(JDKHttpClient.getPayload(mP).toString());
        Assert.assertEquals("Content-Type: multipart/mixed; boundary=\"unique-boundary-1\"\r\n"
                + "\r\n"
                + "This is the preamble area of a multipart message.\n"
                + "Mail readers that understand multipart format\n"
                + "should ignore this preamble.\n"
                + "\n"
                + "If you are reading this text, you might want to\n"
                + "consider changing to a mail reader that understands\n"
                + "how to properly display multipart messages.\n"
                + "\r\n"
                + "--unique-boundary-1\r\n"
                + "\r\n"
                + "... Some text appears here ..."
                + "\r\n"
                + "--unique-boundary-1\r\n"
                + "Content-Type: text/plain; charset=US-ASCII\r\n"
                + "\r\n"
                + "This could have been part of the previous part, but\n"
                + "illustrates explicit versus implicit typing of body\n"
                + "parts.\n"
                + "\r\n"
                + "--unique-boundary-1\r\n"
                + "Content-Type: multipart/parallel; boundary=\"unique-boundary-2\"\r\n"
                + "\r\n"
                + "--unique-boundary-2\r\n"
                + "Content-Type: audio/basic\r\n"
                + "Content-Transfer-Encoding: base64\r\n"
                + "\r\n"
                + "... base64-encoded 8000 Hz single-channel\n"
                + "    mu-law-format audio data goes here ..."
                + "\r\n"
                + "--unique-boundary-2\r\n"
                + "Content-Type: image/jpeg\r\n"
                + "Content-Transfer-Encoding: base64\r\n"
                + "\r\n"
                + "... base64-encoded image data goes here ..."
                + "\r\n"
                + "--unique-boundary-2--\r\n"
                + "\r\n"
                + "--unique-boundary-1\r\n"
                + "Content-Type: text/enriched\r\n"
                + "\r\n"
                + "This is <bold><italic>enriched.</italic></bold>\n"
                + "<smaller>as defined in RFC 1896</smaller>\n"
                + "\n"
                + "Isn't it\n"
                + "<bigger><bigger>cool?</bigger></bigger>\n"
                + "\r\n"
                + "--unique-boundary-1\r\n"
                + "Content-Type: message/rfc822\r\n"
                + "\r\n"
                + "From: (mailbox in US-ASCII)\n"
                + "To: (address in US-ASCII)\n"
                + "Subject: (subject in US-ASCII)\n"
                + "Content-Type: Text/plain; charset=ISO-8859-1\n"
                + "Content-Transfer-Encoding: Quoted-printable\n"
                + "\n"
                + "... Additional text in ISO-8859-1 goes here ...\n"
                + "\r\n"
                + "--unique-boundary-1--\r\n",
                sb.toString());
    }
}
