package org.scribe.extractors;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.model.Request.*;
import org.scribe.test.helpers.*;

public class BaseStringExtractorTest
{

  private BaseStringExtractorImpl extractor;
  private OAuthRequest request;

  @Before
  public void setup()
  {
    request = ObjectMother.createSampleOAuthRequest();
    extractor = new BaseStringExtractorImpl();
  }

  @Test
  public void shouldExtractBaseStringFromOAuthRequest()
  {
    String expected = "GET&http%3A%2F%2Fexample.com&oauth_callback%3Dhttp%253A%252F%252Fexample%252Fcallback%26oauth_consumer_key%3DAS%2523%2524%255E*%2540%2526%26oauth_signature%3DOAuth-Signature%26oauth_timestamp%3D123456";
    String baseString = extractor.extract(request);
    assertEquals(expected, baseString);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfRquestIsNull()
  {
    OAuthRequest nullRequest = null;
    extractor.extract(nullRequest);
  }

  @Test(expected = OAuthParametersMissingException.class)
  public void shouldThrowExceptionIfRquestHasNoOAuthParameters()
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    extractor.extract(request);
  }
}
