package org.scribe.exceptions;

import org.apache.log4j.Logger;
import org.scribe.model.OAuthRequest;

/**
 * Specialized exception that represents a missing OAuth parameter. 
 * 
 * @author Pablo Fernandez
 */
public class OAuthParametersMissingException extends OAuthException
{

	static Logger				logger				= Logger.getLogger("YahooAuthorization");

	private static final long serialVersionUID = 1745308760111976671L;
	private static final String MSG = "Could not find oauth parameters in request: %s. "
		+ "OAuth parameters must be specified with the addOAuthParameter() method";

	/**
	 * Default constructor.
	 * 
	 * @param request OAuthRequest that caused the error
	 */
	public OAuthParametersMissingException(OAuthRequest request)
	{
		super(String.format(MSG, request));
		logger.debug("@@" + String.format(MSG, request));
	}
}
