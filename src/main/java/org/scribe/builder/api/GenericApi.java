package org.scribe.builder.api;

public class GenericApi extends DefaultApi10a {
	private String requestTokenEndpoint;
	private String accessTokenEndpoint;

	@Override
	protected String getRequestTokenEndpoint() {
		return this.requestTokenEndpoint;
	}

	@Override
	protected String getAccessTokenEndpoint() {
		return this.accessTokenEndpoint;
	}
	
	public void setRequestTokenEndpoint(String requestTokenEndpoint) {
		this.requestTokenEndpoint = requestTokenEndpoint;
	}
	
	public void setAccessTokenEndpoint(String accessTokenEndpoint) {
		this.accessTokenEndpoint = accessTokenEndpoint;
	}
}
