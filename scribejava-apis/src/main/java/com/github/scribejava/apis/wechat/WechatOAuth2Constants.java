package com.github.scribejava.apis.wechat;

import com.github.scribejava.core.model.OAuthConstants;

public interface WechatOAuth2Constants extends OAuthConstants {

    String ACCESS_TOKEN_ENDPOINT_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    String REFRESH_TOKEN_ENDPOINT_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

    String CODE_RESPONSE_TYPE = "code";

    String[] SCOPE_VALUES = new String[]{"snsapi_base", "snsapi_userinfo "};

    String CLIENT_ID = "appid";

    String CLIENT_SECRET = "secret";
}
