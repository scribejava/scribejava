package org.scribe.model;

import org.scribe.utils.OAuthEncoder;

/**
 * @author: Pablo Fernandez
 */
public class Parameter implements Comparable<Parameter>
{
  public static final String DISPOSITION_FORM_DATA = "form-data";
  public static final String SEQUENCE_NEW_LINE = "\r\n";
	
  private final String key;
  private final String value;
  private String disposition;

  public Parameter(String key, String value)
  {
    this.key = key;
    this.value = value;
  }

  protected String getKey() {
	return key;
  }

  protected String getValue() {
	return value;
  }

  public String asUrlEncodedPair()
  {
    return OAuthEncoder.encode(key).concat("=").concat(OAuthEncoder.encode(value));
  }
  
  /**
   * Gets the content disposition to be used for this parameter.
   * 
   * @return The content dispoition to be specified when for this parameter.
   */
  public String getDisposition() {
	// If no disposition is set
	if (this.disposition == null) {
		// Assume form data
		this.disposition = DISPOSITION_FORM_DATA;
	}
	return disposition;
  }
	
  /**
   * Sets the content disposition to be used for this parameter.
   * 
   * @param newDisposition
   *            The new content disposition to be set.
   */
  public void setDisposition(final String newDisposition) {
	this.disposition = newDisposition;
  }
	
  /**
   * Encodes this parameter in the multi part format.
   * 
   * @return The string encoded using the multi part format.
   */
  public String asMultiPartEncodedString() {
	final StringBuilder strBldr = new StringBuilder();
	strBldr.append(String.format("Content-Disposition: %1$s; ",
			this.getDisposition()));
	strBldr.append(String.format("name=\"%1$s\"; ", this.getKey()));
	strBldr.append(SEQUENCE_NEW_LINE);
	strBldr.append(SEQUENCE_NEW_LINE);
	strBldr.append(this.getValue());
	strBldr.append(SEQUENCE_NEW_LINE);
	return strBldr.toString();
  }

  /**
   * Indicates whether or not this parameter is to be included in
   * the base string which is generated.
   * @return Whether or not to include this parameter in the base string.
   */
  public boolean usedInBaseString() {
	return true;
  }
	
  public boolean equals(Object other)
  {
    if(other == null) return false;
    if(other == this) return true;
    if(!(other instanceof Parameter)) return false;
    
    Parameter otherParam = (Parameter) other;
    return otherParam.key.equals(key) && otherParam.value.equals(value);
  }

  public int hashCode()
  {
    return key.hashCode() + value.hashCode();
  }

  public int compareTo(Parameter parameter)
  {
    int keyDiff = key.compareTo(parameter.key);

    return keyDiff != 0 ? keyDiff : value.compareTo(parameter.value);
  }
}
