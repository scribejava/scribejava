# Welcome to the home of Scribe-sc, the simple OAuth Java lib forked by the Scenari Team!

### To understand scribe, please read the master github page (https://github.com/fernandezpablo85/scribe-java)

# Why a fork for Scribe ?

Basically, because the support of OAuth 2 by Scribe is not as wide as the standard defines it. This fork aims to enhance the Oauth 2 support, especially by letting the API classes interact with the Oauth2 service to specialize the access token request or sign in request.

### What is different in Scribe-sc

In an API class, you can define how the access token request should be signed.

```java

	@Override
	public ParameterType getClientAuthenticationType() {
	
	// ParameterType.Header => the token cliendId:clientSecret is base64 encoded and send as a Basic Auth
	// ParameterType.QueryString => cliendId and clientSecret are sent as a oauth parameter in the QueryString
	// ParameterType.PostForm => cliendId and clientSecret are sent as a oauth parameter in a URL encoded form
	
		return ParameterType.Header;
	}
	//Default is QueryString for backward compatibility
```

You can define how the oauth 2 parameters should be sent.
```java

	@Override
	public ParameterType getParameterType() {
	
	// ParameterType.Header => the parameters are sent in the header
	// ParameterType.QueryString => the parameters are sent as a QueryString
	// ParameterType.PostForm => The parameters are sent in a URL encoded form
	
		return ParameterType.Header;
	}
	//Default is QueryString for backward compatibility
```

Finally, you can throw the access token request of the service and build your own.
```java

	@Override
	public OAuthRequest handleRequest(OAuthRequest request) {
	//Do what ever you want on the access token request here. The service will not change it. 
	  return request;
	}
```


