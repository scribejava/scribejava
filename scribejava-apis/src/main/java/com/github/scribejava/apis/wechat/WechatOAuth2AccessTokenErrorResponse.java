package com.github.scribejava.apis.wechat;

import com.github.scribejava.core.exceptions.OAuthException;

public class WechatOAuth2AccessTokenErrorResponse extends OAuthException {

    private String errcode;

    private String errmsg;

    private String rawResponse;

    public WechatOAuth2AccessTokenErrorResponse(String message) {
        super(message);
    }

    public WechatOAuth2AccessTokenErrorResponse(String message, String errcode, String errmsg, String rawResponse) {
        super(message);
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.rawResponse = rawResponse;
    }

    public String getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WechatOAuth2AccessTokenErrorResponse that = (WechatOAuth2AccessTokenErrorResponse) o;

        if (errcode != null ? !errcode.equals(that.errcode) : that.errcode != null) {
            return false;
        }
        if (errmsg != null ? !errmsg.equals(that.errmsg) : that.errmsg != null) {
            return false;
        }
        return rawResponse != null ? rawResponse.equals(that.rawResponse) : that.rawResponse == null;

    }

    @Override
    public int hashCode() {
        int result = errcode != null ? errcode.hashCode() : 0;
        result = 31 * result + (errmsg != null ? errmsg.hashCode() : 0);
        result = 31 * result + (rawResponse != null ? rawResponse.hashCode() : 0);
        return result;
    }
}
