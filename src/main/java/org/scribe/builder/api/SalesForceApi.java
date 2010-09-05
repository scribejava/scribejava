package org.scribe.builder.api;

public class SalesForceApi extends DefaultApi10a
{
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://login.salesforce.com/_nc_external/system/security/oauth/AccessTokenHandler";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://login.salesforce.com/_nc_external/system/security/oauth/RequestTokenHandler";
  }
}
