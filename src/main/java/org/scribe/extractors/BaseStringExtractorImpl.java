package org.scribe.extractors;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Default implementation of {@link BaseStringExtractor}. Conforms to OAuth 1.0a
 * 
 * @author Pablo Fernandez
 *
 */
public class BaseStringExtractorImpl implements BaseStringExtractor
{

  private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

  /**
   * {@inheritDoc}
   */
  public String extract(OAuthRequest request)
  {
    checkPreconditions(request);
    String verb = OAuthEncoder.encode(request.getVerb().name());
    String url = OAuthEncoder.encode(request.getSanitizedUrl());
    String params = getSortedAndEncodedParams(request);
    return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
  }

  private String getSortedAndEncodedParams(OAuthRequest request)
  {
    ParameterList params = new ParameterList();
    params.addAll(request.getQueryStringParams());
    params.addAll(request.getBodyParams());
    params.addAll(new ParameterList(request.getOauthParameters()));
    return params.sort().asOauthBaseString();
  }

  private void checkPreconditions(OAuthRequest request)
  {
    Preconditions.checkNotNull(request, "Cannot extract base string from null object");

    if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0)
    {
      throw new OAuthParametersMissingException(request);
    }
  }
}
