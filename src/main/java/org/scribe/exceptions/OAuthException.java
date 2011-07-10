package org.scribe.exceptions;

import org.apache.log4j.Logger;

/**
 * Default scribe exception. 
 * Represents a problem in the OAuth signing process
 * 
 * @author Pablo Fernandez
 */
public class OAuthException extends RuntimeException
{

	static Logger				logger				= Logger.getLogger("YahooAuthorization");

	private static final long serialVersionUID = 1L;

	/**
	 * No-exception constructor. Used when there is no original exception
	 *  
	 * @param message message explaining what went wrong
	 */
	public OAuthException(String message)
	{
		super(message, null);
		logger.debug("@@OAuthException - Token not refreshed - " + message);
	}

	/**
	 * Default constructor 
	 * @param message message explaining what went wrong
	 * @param e original exception
	 */
	public OAuthException(String message, Exception e)
	{
		super(message, e);
		logger.debug("@@OAuthException - Token not refreshed - " + message);

	}
}
