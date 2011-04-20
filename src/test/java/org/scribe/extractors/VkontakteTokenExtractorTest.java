package org.scribe.extractors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;

/**
 * @author Boris G. Tsirkin <mail@dotbg.name>
 * @since 20.4.2011
 */
public class VkontakteTokenExtractorTest {
  VkontakteTokenExtractor extractor;

  @Before
  public void setup() {
    extractor = new VkontakteTokenExtractor();
  }

  @Test
  public void testNormalExtract() throws Exception {
    final String resp = "{\"access_token\":\"533bacf01e11f55b536a565b57531ac1144\",\"expires_in\":43200,\"user_id\":1}";
    final Token result = extractor.extract(resp);
    Assert.assertEquals(result.getToken(), "533bacf01e11f55b536a565b57531ac1144");
    Assert.assertEquals(result.getRawResponse(), resp);
  }

  @Test(expected = OAuthException.class)
  public void shouldThrowExceptionIfTokenIsAbsent() {
    final String response = "{\"expires_in\":43200}";
    extractor.extract("{\"expires_in\":43200}");
  }

  @Test(expected = OAuthException.class)
  public void shouldThrowAnExceptionForNonJSON() {
    final String response = "access_token=166942940015970|2.2ltzWXYND159|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires=5108";
    extractor.extract(response);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfResponseIsNull() {
    extractor.extract(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfResponseIsEmptyString() {
    extractor.extract("");
  }
}
