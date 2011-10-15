package org.scribe.model;

import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;


public class Callback {

    private final static Callback OUT_OF_BAND = new Callback( OAuthConstants.OUT_OF_BAND );
    private final static Callback NONE = new Callback( null );
    
    private final String callbackValue;
    
    private Callback( final String value ) {
        callbackValue = value;
    }
    
    public static Callback from( final String callback ) {
        if ( OUT_OF_BAND.equals( callback ) ) {
            return outOfBand();
        }
        if ( null == callback || "".equals( callback ) ) {
            return none();
        }
        Preconditions.checkValidUrl( callback, "Callback must be a valid URL");
        return new Callback( callback );
    }
    
    public static Callback outOfBand() {
        return OUT_OF_BAND;
    }
    
    public static Callback none() {
        return NONE;
    }
    
    public String getCallbackValue() {
        return callbackValue;
    }

    public String makeURLEncodedValue() {
	return  URLUtils.formURLEncode(callbackValue);
    }
    
    public boolean addToRequest() {
	return this != NONE && this.callbackValue != null;
    }

    public boolean hasValidUrl() {
	return this != NONE && this != OUT_OF_BAND;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((callbackValue == null) ? 0 : callbackValue.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Callback other = (Callback) obj;
	if (callbackValue == null) {
	    if (other.callbackValue != null)
		return false;
	} else if (!callbackValue.equals(other.callbackValue))
	    return false;
	return true;
    }
    
    
    
}
