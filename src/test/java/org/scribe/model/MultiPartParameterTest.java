package org.scribe.model;

import org.junit.Assert;

import org.junit.Test;

public class MultiPartParameterTest {

	private static final String PARAM_KEY = "PARAM_KEY";
	private static final String PARAM_VALUE = "PARAM_VALUE";
	
	private static String multiPartEncode(final String name, final String value, final String disposition) {
		//Build the string expected
		final StringBuilder strBldr = new StringBuilder();
		strBldr.append(String.format("Content-Disposition: %1$s; ", disposition));
		strBldr.append(String.format("name=\"%1$s\"; ", name));
		strBldr.append(Parameter.SEQUENCE_NEW_LINE);
		strBldr.append(Parameter.SEQUENCE_NEW_LINE);
		strBldr.append(value);
		strBldr.append(Parameter.SEQUENCE_NEW_LINE);
		return strBldr.toString();
	}
	
	@Test
	public void shouldUseFormDataDisposition() {
		//Create a new basic parameter
		final Parameter param = new Parameter(PARAM_KEY, PARAM_VALUE);
		//Test default disposition.
		param.setDisposition( null );
		Assert.assertEquals("The disposition does not match.", Parameter.DISPOSITION_FORM_DATA, param.getDisposition());
	}
	
	@Test
	public void shouldUseAttachmentDisposition() {
		//Create a new basic parameter
		final Parameter param = new Parameter(PARAM_KEY, PARAM_VALUE);
		//Change the disposition
		param.setDisposition( Parameter.DISPOSITION_ATTACHMENT );
		Assert.assertEquals("The disposition does not match.", Parameter.DISPOSITION_ATTACHMENT, param.getDisposition());
	}
	
	@Test
	public void shouldProperlyEncodeAsMultipartFormData() {
		//Create a new basic parameter
		final Parameter param = new Parameter(PARAM_KEY, PARAM_VALUE);
		//Use form-data disposition
		param.setDisposition(Parameter.DISPOSITION_FORM_DATA);
		//Build the string expected
		final String formDataEncoded = multiPartEncode(PARAM_KEY, PARAM_VALUE, Parameter.DISPOSITION_FORM_DATA);
		//Test content encoding
		Assert.assertEquals(formDataEncoded, param.asMultiPartEncodedString());
	}
	
	@Test
	public void shouldProperlyEncodeAsMultipartAttachment() {
		//Create a new basic parameter
		final Parameter param = new Parameter(PARAM_KEY, PARAM_VALUE);
		//Use attachment disposition
		param.setDisposition( Parameter.DISPOSITION_ATTACHMENT );
		//Build the string expected
		final String attEncoded = multiPartEncode(PARAM_KEY, PARAM_VALUE, Parameter.DISPOSITION_ATTACHMENT);
		Assert.assertEquals(attEncoded, param.asMultiPartEncodedString());
	}

	@Test
	public void shouldBeInBaseString() {
		//Create a new basic parameter
		final Parameter param = new Parameter(PARAM_KEY, PARAM_VALUE);
		Assert.assertTrue("Regular parameters should be a part of the base string.", param.isUsedInBaseString());
	}
}
