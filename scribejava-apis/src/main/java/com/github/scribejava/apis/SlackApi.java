package com.github.scribejava.apis;

import com.github.scribejava.apis.fitbit.FitBitJsonTokenExtractor;
import com.github.scribejava.apis.slack.SlackJsonTokenExtractor;
import com.github.scribejava.apis.slack.SlackOAuth2AccessToken;
import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Slack.com api
 */
public class SlackApi extends DefaultApi20 {

    protected SlackApi() {
    }

    private static class InstanceHolder {

        private static final SlackApi INSTANCE = new SlackApi();
    }

    public static SlackApi instance() {
        return SlackApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://slack.com/api/oauth.v2.access";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://slack.com/oauth/v2/authorize";
    }

    @Override
    public SlackJsonTokenExtractor getAccessTokenExtractor() {
        return SlackJsonTokenExtractor.instance();
    }
}
