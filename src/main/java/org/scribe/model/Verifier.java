package org.scribe.model;

/**
 * Represents an OAuth verifier code.
 * 
 * @author Pablo Fernandez
 */
public class Verifier
{

  private final String value;

  /**
   * Default constructor.
   * 
   * @param value verifier value
   */
  public Verifier(String value)
  {
	  if (value == null) {
		  throw new IllegalArgumentException();
	  }
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}
