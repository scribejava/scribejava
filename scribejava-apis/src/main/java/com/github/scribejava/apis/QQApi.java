/*
  Copyright 2012 - 2014 Zhang Zhenli

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.utils.OAuthEncoder;


/**
 * This class represents the OAuth API implementation for Tencent QQ Connect using OAuth protocol version 2. It could be part of the Scribe library.
 * <p>
 * <pre>
 * response_type            必须	授权类型，此值固定为“code”。
 * client_id                必须	申请QQ登录成功后，分配给应用的appid。
 * redirect_uri             必须	成功授权后的回调地址，必须是注册appid时填写的主域名下的地址，建议设置为网站首页或网站的用户中心。注意需要将url进行URLEncode。
 * state                    必须	client端的状态值。用于第三方应用防止CSRF攻击，成功授权后回调时会原样带回。请务必严格按照流程检查用户与state参数状态的绑定。
 * scope                    可选	请求用户授权时向用户显示的可进行授权的列表。
 *                              可填写的值是API文档中列出的接口，以及一些动作型的授权（目前仅有：do_like），如果要填写多个接口名称，请用逗号隔开。
 *                              例如：scope=get_user_info,list_album,upload_pic,do_like
 *                              不传则默认请求对接口get_user_info进行授权。
 *                              建议控制授权项的数量，只传入必要的接口名称，因为授权项越多，用户越可能拒绝进行任何授权。
 * display                  可选	仅PC网站接入时使用。用于展示的样式。不传则默认展示为PC下的样式。如果传入“mobile”，则展示为mobile端下的样式。
 * g_ut                     可选	仅WAP网站接入时使用。 QQ登录页面版本（1：wml版本； 2：xhtml版本），默认值为1。
 * </pre>
 *
 * @author Zhang Zhenli
 * @since 1.7.0
 */
public class QQApi extends DefaultApi20 {

    public static final String BASE_URL = "https://graph.qq.com/oauth2.0";
    // Endpont Url.
    public static final String AUTHORIZE_ENDPOINT_URL = BASE_URL + "/authorize";
    public static final String TOKEN_ENDPOINT_URL = BASE_URL + "/token";

    // Url Templet
    public static final String AUTHORIZATION_URL_TEMPLET = AUTHORIZE_ENDPOINT_URL +
            "?client_id=%s" +
            "&redirect_uri=%s" +
            "&response_type=code";

    private static class InstanceHolder {
        private static final QQApi INSTANCE = new QQApi();
    }

    public static QQApi instance() {
        return QQApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        String authorizeUrl = String.format(AUTHORIZATION_URL_TEMPLET,
                config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()));
        if (config.getScope() != null) {
            authorizeUrl = authorizeUrl + "&scope=" + OAuthEncoder.encode(config.getScope());
        }
        return authorizeUrl;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_ENDPOINT_URL + "?grant_type=authorization_code";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }
}
