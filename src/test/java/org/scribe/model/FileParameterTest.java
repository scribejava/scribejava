package org.scribe.model;

import org.junit.Assert;

import java.io.File;

import org.junit.Test;

public class FileParameterTest {

	private static final String PARAM_KEY = "PARAM_KEY";
	private static final String MIME_PLAINTEXT = "text/plain";
	private static final String FILE_CONTENTS = "This is some file content.";
	private static final File testFile = new File("./src/test/test_file.txt");
	private static final File noSuchFile = new File("./file/does_not.exist");
	
	private static String multiPartEncode(final String name,
			final String value, final String disposition, final String mime,
			final String fileName) {
		//Build the string expected
		final StringBuilder strBldr = new StringBuilder();
		strBldr.append(String.format("Content-Disposition: %1$s; ", disposition));
		strBldr.append(String.format("name=\"%1$s\"; ", name));
		strBldr.append( String.format("filename=\"%1$s\"", fileName));
		strBldr.append(Parameter.SEQUENCE_NEW_LINE);
		strBldr.append( String.format("Content-Type: %1$s", mime) );
		strBldr.append(Parameter.SEQUENCE_NEW_LINE);
		strBldr.append(Parameter.SEQUENCE_NEW_LINE);
		strBldr.append(value);
		strBldr.append(Parameter.SEQUENCE_NEW_LINE);
		return strBldr.toString();
	}

	@Test
	public final void shouldGetFileContent() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, testFile);
		//Test file contents
		Assert.assertEquals("Invalid file content.", FILE_CONTENTS, param.getValue());		
	}

	@Test
	public final void shouldGetEmptyString() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, noSuchFile);
		//Test file contents
		Assert.assertEquals("Invalid files should return an empty string.", "", param.getValue());		
	}

	@Test
	public void shouldProperlyEncodeAsMultipartFormData() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, testFile);
		//Use form-data disposition
		param.setDisposition(Parameter.DISPOSITION_FORM_DATA);
		//Build the string expected
		final String formDataEncoded = multiPartEncode(PARAM_KEY, FILE_CONTENTS, Parameter.DISPOSITION_FORM_DATA, param.getMimeType(), testFile.getName());
		//Test content encoding
		Assert.assertEquals("Parameter incorrectly encoded.", formDataEncoded, param.asMultiPartEncodedString());
	}
	
	@Test
	public void shouldProperlyEncodeAsMultipartAttachment() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, testFile);
		//Use attachment disposition
		param.setDisposition( Parameter.DISPOSITION_ATTACHMENT );
		//Build the string expected
		final String attEncoded = multiPartEncode(PARAM_KEY, FILE_CONTENTS, Parameter.DISPOSITION_ATTACHMENT, param.getMimeType(), testFile.getName());
		Assert.assertEquals("Parameter incorrectly encoded.", attEncoded, param.asMultiPartEncodedString());
	}

	@Test
	public final void testIsUsedInBaseString() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, testFile);
		Assert.assertFalse("File parameters should NOT be a part of the base string.", param.isUsedInBaseString());
	}

	@Test
	public final void testGetFileName() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, testFile);
		Assert.assertEquals("File name incorrect.", testFile.getName(), param.getFileName());
	}

	@Test
	public final void testGetMimeType() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, testFile);
		Assert.assertEquals("MIME-Type incorrect.", FileParameter.DEFAULT_MIME_TYPE, param.getMimeType());
	}

	@Test
	public final void testSetMimeType() {
		//Create a new file parameter
		final FileParameter param = new FileParameter(PARAM_KEY, testFile);
		//Change the mime type
		param.setMimeType(MIME_PLAINTEXT);
		Assert.assertEquals("MIME-Type incorrect.", MIME_PLAINTEXT, param.getMimeType());
	}

}
