package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class EveApi extends DefaultApi20 {

	public EveApi() {
	}
	
	private static class InstanceHolder {
		private static final EveApi INSTANCE = new EveApi();
	}
	
	public static EveApi instance() {
		return EveApi.InstanceHolder.INSTANCE;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "https://login.eveonline.com/v2/oauth/token/";
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return "https://login.eveonline.com/v2/oauth/authorize/";
	}

}
