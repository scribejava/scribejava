/**
 * 
 */
package org.scribe.model;

/**
 * @author mcxiaoke
 * 
 */
public enum ResponseType {
	CODE("code"), TOKEN("token"), CODE_AND_TOKEN("code_and_token");

	private String typeValue;

	private ResponseType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return typeValue;
	}
}
