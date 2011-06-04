package org.scribe.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Token;

public class MailruTokenExtractorTest
{

  private MailruTokenExtractor extractor;

  @Before
  public void setup()
  {
    extractor = new MailruTokenExtractor();
  }

  @Test
  public void shouldExtractTokenFromOAuthStandardResponse()
  {
    String tokenValue = "MaIlRuToKeN";
    String response = "{\n" +
                      "    \"access_token\":\"" + tokenValue + "\",\n" +
                      "}";
    Token extracted = extractor.extract(response);
    assertEquals(tokenValue, extracted.getToken());
    assertEquals("", extracted.getSecret());
  }

  @Test
  public void shouldExtractTokenFromResponseWithExpiresParam()
  {
    String response = "{\n" +
                      "    \"access_token\":\"166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE\",\n" +
                      "    \"expires\":\"5108\"\n" +
                      "}";
    Token extracted = extractor.extract(response);
    assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE", extracted.getToken());
    assertEquals("", extracted.getSecret());
  }
}
