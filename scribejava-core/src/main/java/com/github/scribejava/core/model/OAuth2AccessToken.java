/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.scribejava.core.model;

/**
 * https://tools.ietf.org/html/rfc6749#section-4.1.4
 * 
 * @author tyreus
 */
public class OAuth2AccessToken implements AccessToken {
    
    private final String accessToken;
    private final String tokenType;
    private final String refreshToken;
    private final Long expiresIn;
    private final String rawResponse;

    public OAuth2AccessToken(String accessToken, String tokenType, String refreshToken, Long expiresIn, String rawResponse) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.rawResponse = rawResponse;
    }
    
    public OAuth2AccessToken(String accessToken, String rawResponse) {
        this.accessToken = accessToken;
        this.tokenType = null;
        this.refreshToken = null;
        this.expiresIn = null;
        this.rawResponse = rawResponse;
    }

    @Override
    public String getToken() {
        return accessToken;
    }

    @Override
    public boolean isEmpty() {
        if (accessToken == null) {
            return true;
        }
        return accessToken.isEmpty();
    }

    
}
