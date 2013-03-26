package org.scribe.model;

public enum GrantType {
	AUTHORIZATION_CODE("authorization_code"), RESOURCE_OWNER_PASSWORD_CREDENTIALS(
			"password"), IMPLICIT(""), REFRESH_TOKEN("refresh_token");

	private String typeValue;

	private GrantType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return typeValue;
	}
}
