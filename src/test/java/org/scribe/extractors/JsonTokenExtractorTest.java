package org.scribe.extractors;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.model.*;

public class JsonTokenExtractorTest {
	private String response = "{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\"}";
	
	private String response2 = "{\n"
			+ "\"access_token\" : \"ya29.AHES6ZS2CXmyWMdMcxp5XkqtF7ffQjavxKIBd3k9LO8NWN9\",\n"
			+ "\"token_type\" : \"Bearer\",\n"
			+ "\"expires_in\" : 3600,\n"
			+ "\"id_token\" : \"eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXVkIjoiMzU3NjQyMTUzOTUxLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiY2lkIjoiMzU3NjQyMTUzOTUxLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiaWQiOiIxMDU3ODUyODQzMjY3NTQ0Nzc1NjIiLCJ0b2tlbl9oYXNoIjoiZGlmemgyaTBQaU9pTUJIZE5SSjM3ZyIsImhkIjoiZmVlZHRoZWNvZmZlcnMub3JnLnVrIiwiaWF0IjoxMzIyNjY3MDU1LCJleHAiOjEzMjI2NzA5NTV9.AEOoU2-sr44V5xxE612z5An8vQts9ymFtPMOLSo9zx_I91qH8GfN1IWqgMVgR3rq7yHG7fpKM9IoffXYMcvCiJYVGMLblXTejtyVg1EAwrGr5W9s7R6qsGlWZIMTWa61_y4mnEKhsK1tP3svVE47uswSErW2u-OTIp-_4SyY8W4\",\n"
			+ "\"refresh_token\" : \"1/ZpP1OnQDJEWzuZ-58fglCr_Fvq2iN5m23oTj8GIBBb1\"\n}";
	
	private JsonTokenExtractor extractor = new JsonTokenExtractor();

	@Test
	public void shouldParseResponse() {
		Token token = extractor.extract(response);
		assertEquals(token.getToken(),
				"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X");
	}

	@Test
	public void shouldParseResponseWithSpaces() {
		Token token = extractor.extract(response2);
		assertEquals(token.getToken(),
				"ya29.AHES6ZS2CXmyWMdMcxp5XkqtF7ffQjavxKIBd3k9LO8NWN9");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfForNullParameters() {
		extractor.extract(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfForEmptyStrings() {
		extractor.extract("");
	}
}
