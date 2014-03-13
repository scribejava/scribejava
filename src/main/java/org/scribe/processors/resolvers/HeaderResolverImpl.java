package org.scribe.processors.resolvers;

import java.util.*;

import org.scribe.exceptions.*;
import org.scribe.http.OAuthRequest;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Default implementation of {@link org.scribe.processors.resolvers.Resolver}.
 * 
 * @author Pablo Fernandez
 *
 */
public class HeaderResolverImpl implements Resolver
{
  private static final String PARAM_SEPARATOR = ", ";
  private static final String PREAMBLE = "OAuth ";
  public static final int ESTIMATED_PARAM_LENGTH = 20;

  /**
   * {@inheritDoc}
   */
  public String extract(OAuthRequest request)
  {
    checkPreconditions(request);
    Map<String, String> parameters = request.getOauthParameters();
    StringBuilder header = new StringBuilder(parameters.size() * ESTIMATED_PARAM_LENGTH);
    header.append(PREAMBLE);
    for (Map.Entry<String, String> entry : parameters.entrySet())
    {
      if(header.length() > PREAMBLE.length())
      { 
        header.append(PARAM_SEPARATOR);
      }
      header.append(String.format("%s=\"%s\"", entry.getKey(), OAuthEncoder.encode(entry.getValue())));
    }
    
    if (request.getRealm() != null && !request.getRealm().isEmpty())
    {
      header.append(PARAM_SEPARATOR);
      header.append(String.format("%s=\"%s\"", OAuthConstants.REALM, request.getRealm()));
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
