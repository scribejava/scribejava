package com.github.scribejava.apis.wechat;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

import java.util.regex.Pattern;

/**
 * additionally parses openId openid and unionId unionid.
 */
public class WeChatJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern OPEN_ID_REGEX_PATTERN = Pattern.compile("\"openid\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern UNION_ID_REGEX_PATTERN = Pattern.compile("\"unionid\"\\s*:\\s*\"(\\S*?)\"");

    protected WeChatJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final WeChatJsonTokenExtractor INSTANCE = new WeChatJsonTokenExtractor();
    }

    public static WeChatJsonTokenExtractor instance() {
        return WeChatJsonTokenExtractor.InstanceHolder.INSTANCE;
    }

    @Override
    protected WeChatToken createToken(String accessToken, String tokenType, Integer expiresIn,
                                      String refreshToken, String scope, String response) {
        return new WeChatToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, OPEN_ID_REGEX_PATTERN, false),
                extractParameter(response, UNION_ID_REGEX_PATTERN, false), response);
    }
}
