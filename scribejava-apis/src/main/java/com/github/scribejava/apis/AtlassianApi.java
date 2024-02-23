package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class AtlassianApi extends DefaultApi20 {

    protected AtlassianApi() {
    }

   private static class InstanceHolder {
       
         private static final AtlassianApi INSTANCE = new AtlassianApi();
     }
 
     public static AtlassianApi instance() {
         return InstanceHolder.INSTANCE;
     }
 
     @Override
     public String getAccessTokenEndpoint() {
         return "https://auth.atlassian.com/oauth/token";
     }
 
     @Override
     protected String getAuthorizationBaseUrl() {
         return "https://auth.atlassian.com/authorize";
     }
}
