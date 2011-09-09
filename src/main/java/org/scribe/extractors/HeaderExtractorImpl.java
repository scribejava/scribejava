package org.scribe.extractors;

import java.util.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Default implementation of {@link HeaderExtractor}. Conforms to OAuth 1.0a
 * 
 * @author Pablo Fernandez
 *
 */
public class HeaderExtractorImpl implements HeaderExtractor
{
  private static final String PARAM_SEPARATOR = ", ";
  private static final String PREAMBLE = "OAuth ";

  /**
   * {@inheritDoc}
   */
  public String extract(OAuthRequest request)
  {
    checkPreconditions(request);
    List<Param> parameters = request.getOauthParameters();
    StringBuffer header = new StringBuffer(parameters.size() * 20);
    header.append(PREAMBLE);
    for (Param entry : parameters)
    {
      if(header.length() > PREAMBLE.length())
      { 
        header.append(PARAM_SEPARATOR);
      }
      header.append(String.format("%s=\"%s\"", entry.getKey(), URLUtils.percentEncode(entry.getValue())));
    }
    return header.toString();
  }

  private void checkPreconditions(OAuthRequest request)
  {
    Preconditions.checkNotNull(request, "Cannot extract a header from a null object");

    if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0)
    {
      throw new OAuthParametersMissingException(request);
    }
  }

}
