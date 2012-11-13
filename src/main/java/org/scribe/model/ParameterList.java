package org.scribe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * @author: Pablo Fernandez
 */
public class ParameterList
{
  private static final char QUERY_STRING_SEPARATOR = '?';
  private static final String PARAM_SEPARATOR = "&";
  private static final String PAIR_SEPARATOR = "=";
  private static final String EMPTY_STRING = "";

  private static String boundary;

  private final List<Parameter> params;

  public ParameterList()
  {
    params = new ArrayList<Parameter>();
  }

  ParameterList(List<Parameter> params)
  {
    this.params = new ArrayList<Parameter>(params);
  }

  public ParameterList(Map<String, String> map)
  {
    this();
    for(Map.Entry<String, String> entry : map.entrySet())
    {
      params.add(new Parameter(entry.getKey(), entry.getValue()));
    }
  }
  
  public void add(String key, String value)
  {
	  this.add(new Parameter(key, value));
  }

  /**
   * Adds a new parameter to the content of this list.
   * 
   * @param newParam
   *            The new parameter to be added.
   */
  public void add(Parameter newParam) {
	// If the new parameter is valid
	if (newParam != null) {
		// Add it to the list.
		params.add(newParam);
	}
  }

  public String appendTo(String url)
  {
    Preconditions.checkNotNull(url, "Cannot append to null URL");
    String queryString = asFormUrlEncodedString();
    if (queryString.equals(EMPTY_STRING))
    {
      return url;
    }
    else
    {
      url += url.indexOf(QUERY_STRING_SEPARATOR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
      url += queryString;
      return url;
    }
  }

  public String asOauthBaseString()
  {
		if (params.size() == 0) {
			return EMPTY_STRING;
		}
		StringBuilder builder = new StringBuilder();
		for (Parameter p : params) {
			if (p.usedInBaseString()) {
				builder.append('&').append(p.asUrlEncodedPair());
			}
		}
		// Remove the first &
		builder.delete(0, 1);
		return OAuthEncoder.encode(builder.toString());
  }

  public String asFormUrlEncodedString()
  {
    if (params.size() == 0) return EMPTY_STRING;

    StringBuilder builder = new StringBuilder();
    for(Parameter p : params)
    {
      builder.append('&').append(p.asUrlEncodedPair());
    }
    return builder.toString().substring(1);
  }

  /**
   * Encodes the content of this list according to the  
   * @return
   */
  public String asMultiPartEncodedString() {
	  if (params.size() == 0) {
		  return EMPTY_STRING;
	  }	
	  final String paramSep = new StringBuilder("--").append(getBoundary()).append(Parameter.SEQUENCE_NEW_LINE).toString();
	  StringBuilder builder = new StringBuilder();
	  builder.append(paramSep);
	  //Add the parameters
	  for (Parameter p : params) {
		//Add the encoded string.
		builder.append( p.asMultiPartEncodedString() );
		//Add the boundrary.
		builder.append(paramSep);
	  }
	  builder.append(Parameter.SEQUENCE_NEW_LINE);
	  builder.append("--");
	  builder.append( getBoundary() );
	  builder.append( "--" );
	  builder.append(Parameter.SEQUENCE_NEW_LINE);
	  return builder.toString();	
  }
	
  /**
   * Gets the boundary to be used when building the body of the request.
   * 
   * @return The boundary to be used in the body of multi-part requests.
   */
  public static String getBoundary() {
	if (boundary == null) {
		final int radix = 36;
		final Random randGen = new Random();
		final StringBuilder strBldr = new StringBuilder();
		strBldr.append(Long.toString(randGen.nextLong(), radix));
			boundary = strBldr.toString();
	}
	return boundary;
  }

  public void addAll(ParameterList other)
  {
    params.addAll(other.params);
  }

  public void addQuerystring(String queryString)
  {
    if (queryString != null && queryString.length() > 0)
    {
      for (String param : queryString.split(PARAM_SEPARATOR))
      {
        String pair[] = param.split(PAIR_SEPARATOR);
        String key = OAuthEncoder.decode(pair[0]);
        String value = pair.length > 1 ? OAuthEncoder.decode(pair[1]) : EMPTY_STRING;
        params.add(new Parameter(key, value));
      }
    }
  }

  public boolean contains(Parameter param)
  {
    return params.contains(param);
  }

  public int size()
  {
    return params.size();
  }

  public ParameterList sort()
  {
    ParameterList sorted = new ParameterList(params);
    Collections.sort(sorted.params);
    return sorted;
  }
}
