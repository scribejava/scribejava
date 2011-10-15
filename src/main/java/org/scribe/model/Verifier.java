package org.scribe.model;

import org.scribe.utils.*;

/**
 * Represents an OAuth verifier code.
 * 
 * @author Pablo Fernandez
 */
public class Verifier
{

  private final static Verifier NONE = new Verifier();
    
  private final String value;

  /**
   * Default constructor.
   * 
   * @param value verifier value
   */
  public Verifier(String value)
  {
    Preconditions.checkNotNull(value, "Must provide a valid string as verifier");
    this.value = value;
  }
  
  private Verifier() {
      this.value = null;
  }

  public String getValue()
  {
    return value;
  }
  
  public static Verifier none() {
      return NONE;
  }

  public boolean isDefined() {
    return this != NONE;
  }
}
