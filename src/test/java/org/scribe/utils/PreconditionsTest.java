package org.scribe.utils;

import org.junit.*;

public class PreconditionsTest
{

  private static final String ERROR_MSG = "";

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullObjects()
  {
    Preconditions.checkNotNull(null, ERROR_MSG);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullStrings()
  {
    Preconditions.checkEmptyString(null, ERROR_MSG);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForEmptyStrings()
  {
    Preconditions.checkEmptyString("", ERROR_MSG);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForSpacesOnlyStrings()
  {
    Preconditions.checkEmptyString("               ", ERROR_MSG);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForInvalidUrls()
  {
    Preconditions.checkValidUrl("this/is/not/a/valid/url", ERROR_MSG); 
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullUrls()
  {
    Preconditions.checkValidUrl(null, ERROR_MSG);
  }
  
  @Test
  public void shouldAllowValidUrls()
  {
    Preconditions.checkValidUrl("http://www.example.com", ERROR_MSG);
  }
  
  @Test
  public void shouldAllowSSLUrls()
  {
    Preconditions.checkValidUrl("https://www.example.com", ERROR_MSG);
  }

  @Test
  public void shouldAllowSpecialCharsInScheme() 
  {
    Preconditions.checkValidUrl("custom+9.3-1://www.example.com", ERROR_MSG);
  }
  
  @Test
  public void shouldAllowNonStandarProtocolsForAndroid()
  {
    Preconditions.checkValidUrl("x-url-custom://www.example.com", ERROR_MSG);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowStrangeProtocolNames()
  {
    Preconditions.checkValidUrl("$weird*://www.example.com", ERROR_MSG);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowUnderscoreInScheme()
  {
    Preconditions.checkValidUrl("http_custom://www.example.com", ERROR_MSG);
  }
  
  @Test
  public void shouldAllowOutOfBandAsValidCallbackValue()
  {
    Preconditions.checkValidOAuthCallback("oob", ERROR_MSG);
  }
}
