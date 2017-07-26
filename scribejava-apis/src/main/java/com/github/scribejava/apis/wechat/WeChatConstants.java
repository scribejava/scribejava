package com.github.scribejava.apis.wechat;

import com.github.scribejava.core.model.OAuthConstants;

/**
 * This class contains OAuth constants, Custom for WeChat.
 */
public interface WeChatConstants extends OAuthConstants {

    // WeChat's client_id is called appid and client_secret is called secret
    String CLIENT_ID = "appid";
    String CLIENT_SECRET = "secret";

    String LANG = "lang";
    String OPEN_ID = "openid";
    String UNION_ID = "unionid";

}
