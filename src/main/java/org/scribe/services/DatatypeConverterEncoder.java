package org.scribe.services;

public class DatatypeConverterEncoder extends Base64Encoder
{
  @Override
  public String encode(byte[] bytes)
  {
	return Base64Encoder.getInstance().encode(bytes);    
  }

  @Override
  public byte[] encodeBase64(byte[] bytes)
  {
    return Base64Encoder.getInstance().encodeBase64(bytes);    
  }
  
  @Override
  public String getType()
  {
    return "DatatypeConverter";
  }
}
