package org.scribe.extractors;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.test.helpers.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class HeaderExtractorTest
{

  private HeaderExtractorImpl extractor;
  private OAuthRequest request;

  @Before
  public void setup()
  {
    request = ObjectMother.createSampleOAuthRequest();
    extractor = new HeaderExtractorImpl();
  }

  @Test
  public void shouldExtractStandardHeader()
  {
    String header = extractor.extract(request);

    assertThat(header, containsString("oauth_callback=\"http%3A%2F%2Fexample%2Fcallback\""));
    assertThat(header, containsString("oauth_signature=\"OAuth-Signature\""));
    assertThat(header, containsString("oauth_consumer_key=\"AS%23%24%5E%2A%40%26\""));
    assertThat(header, containsString("oauth_timestamp=\"123456\""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldExceptionIfRequestIsNull()
  {
    OAuthRequest nullRequest = null;
    extractor.extract(nullRequest);
  }

  @Test(expected = OAuthParametersMissingException.class)
  public void shouldExceptionIfRequestHasNoOAuthParams()
  {
    OAuthRequest emptyRequest = new OAuthRequest(Verb.GET, "http://example.com");
    extractor.extract(emptyRequest);
  }
}
