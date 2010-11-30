package org.scribe.model;

import org.scribe.utils.*;

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
    this.value = URLUtils.percentDecode(value);
  }

  public String getValue()
  {
    return value;
  }
}
