# Welcome to the home of ScribeJava, the simple OAuth Java lib!

# Why use ScribeJava?

### Dead Simple

Who said OAuth/OAuth2 was difficult? Configuring ScribeJava is __so easy your grandma can do it__! check it out:

```java
OAuthService service = new ServiceBuilder()
                                  .apiKey(YOUR_API_KEY)
                                  .apiSecret(YOUR_API_SECRET)
                                  .build(LinkedInApi20.instance());
```

That **single line** (added newlines for readability) is the only thing you need to configure ScribeJava with LinkedIn's OAuth API for example.

### Threadsafe

Hit ScribeJava as hard and with many threads as you like.

### Async

You can use ning async http client out-of-box, just use ServiceBuilderAsync

### Supports all major 1.0a and 2.0 OAuth APIs out-of-the-box

* Google

* Facebook

* Yahoo

* LinkedIn

* Twitter

* Foursquare

* Evernote

* Vimeo

* Windows Live

* Odnoklassniki

* Mail.ru

* LinkedIn2.0

* Google2.0

* GitHub

* and many more! check the [examples folder](https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples)

### Small and modular

ScribeJava's code is small (about 1k LOC) and simple to understand. No smart-ass or "clever" hacks here.
You can use only 'core' or 'with apis' maven modules

### Android-Ready

Works out of the box with android(TM) applications.

### Stable & bulletproof

Good test coverage to keep you safe from harm.

When something bad actually happens, ScribeJava's meaningful error messages will tell you exactly what went wrong, when and where.

### Pull it from Maven Central!

You can pull ScribeJava from the central maven repository, just add these to your __pom.xml__ file:

```xml
<dependency>
    <groupId>com.github.scribejava</groupId>
    <artifactId>scribejava-apis</artifactId>
    <version>2.5.3</version>
</dependency>
```

And in case you need just core classes (that's it, without any external API (FB, VK, GitHub, Google etc) specific code), you could pull just 'core' artifact.
```xml
<dependency>
    <groupId>com.github.scribejava</groupId>
    <artifactId>scribejava-core</artifactId>
    <version>2.5.3</version>
</dependency>
```

## Getting started in less than 2 minutes

Check the [Getting Started](https://github.com/scribejava/scribejava/wiki/getting-started) page and start rocking! Please Read the [FAQ](https://github.com/scribejava/scribejava/wiki/faq) before creating an issue :)

Also, remember to read the [fantastic tutorial](http://akoskm.github.io/2015/07/31/twitter-sign-in-for-web-apps.html) that [@akoskm](https://twitter.com/akoskm) wrote to easily integrate a server side app with an API (twitter in this case).

## Questions?

Feel free to drop us an email or create issue right here on github.com

## Forks

If you have a useful fork that should be listed there please contact us


## QQ登陆

        final String apiKey = "101303927";
        final String apiSecret = "0c3ac6430d6e2f60dfb637101252417e ";
        final OAuth20Service service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                .callback("http://www.yichisancun.com/qqlogin.htm").state("xxxx")
                .scope("get_user_info,list_album,upload_pic,do_like").build(QQApi.instance());
        System.out.println(service.getAuthorizationUrl())

## 微信登陆

        final String apiKey = "x";
        final String apiSecret = "x ";
        final OAuth20Service service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                .callback("url").state("xxxx").scope("snsapi_login")
                .build(WeiXinApi.instance());
        System.out.println(service.getAuthorizationUrl());

## 开源中国登陆

     final OAuth20Service    oschina = new ServiceBuilder().apiKey("CTJlkYcnBaZCsi4GGgUk").grantType("authorization_code")
                .apiSecret("TlKrmPCKImAKEzk1ORZtdwooJKDIgXrF").callback("http://www.yichisancun.com/oschinalogin.htm")
                .responseType("code").build(OschinaApi.instance());
                System.out.println(service.getAuthorizationUrl());

    