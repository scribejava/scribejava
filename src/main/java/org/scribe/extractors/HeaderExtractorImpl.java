package org.scribe.extractors;

import java.util.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class HeaderExtractorImpl implements HeaderExtractor
{

  private static final String PREAMBLE = "OAuth ";

  public String extract(OAuthRequest request)
  {
    checkPreconditions(request);
    Map<String, String> parameters = request.getOauthParameters();
    StringBuffer header = new StringBuffer();
    header.append(PREAMBLE);
    for (String key : parameters.keySet())
    {
      addToHeader(header, key, parameters.get(key));
    }
    return removeTrail(header);
  }

  private void checkPreconditions(OAuthRequest request)
  {
    Preconditions.checkNotNull(request, "Cannot extract a header from a null object");

    if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0)
    {
      throw new OAuthParametersMissingException(request);
    }
  }

  private void addToHeader(StringBuffer header, String name, String value)
  {
    header.append(String.format("%s=\"%s\", ", name, URLUtils.percentEncode(value)));
  }

  private String removeTrail(StringBuffer header)
  {
    return header.toString().substring(0, header.length() - 2);
  }
}
