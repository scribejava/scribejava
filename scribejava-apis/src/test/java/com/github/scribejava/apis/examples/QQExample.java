package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.QQApi;
import com.github.scribejava.apis.SinaWeiboApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

public class QQExample {

	public static void main(String[] args) {

		final String apiKey = "101303927";
		final String apiSecret = "0c3ac6430d6e2f60dfb637101252417e ";
		final OAuth20Service service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
				.callback("http://www.yichisancun.com/qqlogin.htm").state("xxxx")
				.scope("get_user_info,list_album,upload_pic,do_like").build(QQApi.instance());

		System.out.println(service.getAuthorizationUrl());
	}
}
