package org.scribe.extractors;

/**
 * {@link AccessTokenExtractor} for Mail.ru OAuth API
 *
 */
public class MailruTokenExtractor extends TokenExtractor20Impl
{

  public MailruTokenExtractor()
  {
    super("\\{(?:\\s|.)*?\"access_token\"\\s*:\\s*\"([^\"]*)\"(?:\\s|.)*\\}", false);
  }

}
