package com.github.scribejava.core.model;

import com.github.scribejava.core.oauth.OAuth20Service;

public interface OAuth2CodeProvider {

    String getCode(OAuth20Service service);
}
