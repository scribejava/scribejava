package com.github.scribejava.apis.wechat;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.Preconditions;

import java.io.IOException;
import java.util.regex.Pattern;

public class WechatOAuth2AccessTokenJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern ERRCODE_REGEX_PATTERN = Pattern.compile("\"errcode\"\\s*:\\s*([0-9]+)");
    private static final Pattern ERRMSG_REGEX_PATTERN = Pattern.compile("\"errmsg\"\\s*:\\s*\"([\\S\\s]*?)\"");
    private static final Pattern OPENID_REGEX_PATTERN = Pattern.compile("\"openid\"\\s*:\\s*\"(\\S*?)\"");

    protected WechatOAuth2AccessTokenJsonExtractor() {
    }

    private static class InstanceHolder {
        private static final WechatOAuth2AccessTokenJsonExtractor INSTANCE = new WechatOAuth2AccessTokenJsonExtractor();
    }

    public static WechatOAuth2AccessTokenJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public OAuth2AccessToken extract(Response response) throws IOException {
        checkError(response.getBody());
        return super.extract(response);
    }

    /**
     * Try to extract the error information in response. If 'errcode' or 'errmsg' is found,
     * {@link WechatOAuth2AccessTokenErrorResponse} will be thrown, otherwise execute super method.
     */
    private void checkError(final String response) {
        if (!Preconditions.hasText(response)) {
            return;
        }
        final String errcode = extractParameter(response, ERRCODE_REGEX_PATTERN, false);
        final String errmsg = extractParameter(response, ERRMSG_REGEX_PATTERN, false);

        if (errcode != null || errmsg != null) {
            throw new WechatOAuth2AccessTokenErrorResponse("Obtaining WeChat OAuth2 access_token failed.", errcode, errmsg, response);
        }
    }

    @Override
    public void generateError(String response) {
        throw new WechatOAuth2AccessTokenErrorResponse("An unknown failure occurred while obtaining the WeChat OAuth2 access_token.");
    }

    @Override
    protected WechatOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope, String response) {
        final String openid = extractParameter(response, OPENID_REGEX_PATTERN, true);
        return new WechatOAuth2AccessToken(accessToken, expiresIn, refreshToken, scope, openid, response);
    }
}
