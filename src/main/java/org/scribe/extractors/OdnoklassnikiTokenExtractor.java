package org.scribe.extractors;

/**
 * {@link AccessTokenExtractor} for odnoklassniki.ru OAuth API
 *
 */
public class OdnoklassnikiTokenExtractor extends TokenExtractor20Impl
{

  public OdnoklassnikiTokenExtractor()
  {
    super("\\{(?:\\s|.)*?\"access_token\"\\s*:\\s*\"([^\"]*)\"(?:\\s|.)*\\}", false);
  }

}
