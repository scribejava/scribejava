package org.scribe.model;

import java.util.Map;

public interface OAuthBaseRequest {

    public void addOAuthParameter(String key, String value);
    public Map<String, String> getOauthParameters();

    public String getCompleteUrl();
    public void addHeader(String key, String value);
    public void addQuerystringParameter(String key, String value);
    public String getSanitizedUrl();
    public ParameterList getQueryStringParams();
    public Verb getVerb();
    public ParameterList getBodyParams();

    public String toString();
}
