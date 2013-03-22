package org.scribe.model;

public enum SignatureType {
	HEADER_BEARER("Bearer"), HEADER_OAUTH("OAuth2"), HEADER_MAC("MAC"), QUERY_STRING("QueryString");
	
	private String typeValue;

	private SignatureType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return typeValue;
	}
}
