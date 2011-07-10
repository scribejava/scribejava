package org.scribe.exceptions;

import org.apache.log4j.Logger;

/**
 * Specialized exception that represents a problem in the signature
 * 
 * @author Pablo Fernandez
 */
public class OAuthSignatureException extends OAuthException
{

	static Logger				logger				= Logger.getLogger("YahooAuthorization");
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 * @param stringToSign plain string that gets signed (HMAC-SHA, etc)
	 * @param e original exception
	 */
	public OAuthSignatureException(String stringToSign, Exception e)
	{
		super("Error while signing string: " + stringToSign, e);
		logger.debug("@@Error while signing string: " + stringToSign, e);
	}

}
