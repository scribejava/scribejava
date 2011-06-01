package org.scribe.extractors;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Token;

public class OdnoklassnikiTokenExtractorTest
{

  private OdnoklassnikiTokenExtractor extractor;

  @Before
  public void setup()
  {
    extractor = new OdnoklassnikiTokenExtractor();
  }

  @Test
  public void shouldExtractTokenFromOAuthStandardResponse()
  {
    String tokenValue = "OdnoklassnikiToken";
    String response = "{\"access_token\":\"" + tokenValue + "\"}";
    Token extracted = extractor.extract(response);
    assertEquals(tokenValue, extracted.getToken());
    assertEquals("", extracted.getSecret());
  }

  @Test
  public void shouldExtractTokenFromResponseWithExpiresParam()
  {
    String response = "{\"access_token\":\"166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE\",\"expires\":\"5108\"}";
    Token extracted = extractor.extract(response);
    assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE", extracted.getToken());
    assertEquals("", extracted.getSecret());
  }
}
