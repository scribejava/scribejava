# Welcome to the home of ScribeJava, the simple OAuth client Java lib!

[![Build Status](https://travis-ci.org/scribejava/scribejava.svg?branch=master)](https://travis-ci.org/scribejava/scribejava)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.scribejava/scribejava/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.scribejava/scribejava)


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

Working runnable examples are [here](https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples)

### Threadsafe

Hit ScribeJava as hard and with many threads as you like.

### Async and other HTTP clients

ScribeJava support out-of-box several HTTP clients:
 * ning async http client 1.9.x (maven module scribejava-httpclient-ning)
 * asynchttpclient 2.x (maven module scribejava-httpclient-ahc)
 * OkHttp (maven module scribejava-httpclient-okhttp)
 
 just add corresponding maven modules to your pom

### Supports all major 1.0a and 2.0 OAuth APIs out-of-the-box

* AWeber (http://www.aweber.com/)
* Box (https://www.box.com/)
* Digg (http://digg.com/)
* Доктор на работе (https://www.doktornarabote.ru/)
* Facebook (https://www.facebook.com/)
* Flickr (https://www.flickr.com/)
* Foursquare (https://foursquare.com/)
* Freelancer (https://www.freelancer.com/)
* Genius (http://genius.com/)
* GitHub (https://github.com/)
* Google (https://www.google.com/)
* HeadHunter ХэдХантер (https://hh.ru/)
* Imgur (http://imgur.com/)
* Kaixin 开心网 (http://www.kaixin001.com/)
* LinkedIn (https://www.linkedin.com/)
* Microsoft Live (https://login.live.com/)
* Mail.Ru (https://mail.ru/)
* Meetup (http://www.meetup.com/)
* NAVER (http://www.naver.com/)
* NetEase (http://www.163.com/)
* Odnoklassniki Одноклассники (http://ok.ru/)
* Pinterest (https://www.pinterest.com/)
* 500px (https://500px.com/)
* Renren (http://renren.com/)
* Salesforce (https://www.salesforce.com/)
* Sina (http://www.sina.com.cn/ http://weibo.com/login.php)
* Skyrock (http://skyrock.com/)
* sohu 搜狐 (http://www.sohu.com/)
* StackExchange (http://stackexchange.com/)
* The Things Network (v1-staging and v2-preview) (https://www.thethingsnetwork.org/)
* Trello (https://trello.com/)
* Tumblr (https://www.tumblr.com/)
* TUT.BY (http://www.tut.by/)
* Twitter (https://twitter.com/)
* Viadeo (http://viadeo.com/)
* VK ВКонтакте (http://vk.com/)
* XING (https://www.xing.com/)
* Yahoo (https://www.yahoo.com/)
* Misfit (http://misfit.com/)
* check the [examples folder](https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples)

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
    <version>4.1.1</version>
</dependency>
```

And in case you need just core classes (that's it, without any external API (FB, VK, GitHub, Google etc) specific code), you could pull just 'core' artifact.
```xml
<dependency>
    <groupId>com.github.scribejava</groupId>
    <artifactId>scribejava-core</artifactId>
    <version>4.1.1</version>
</dependency>
```

## Getting started in less than 2 minutes

Check the [Getting Started](https://github.com/scribejava/scribejava/wiki/getting-started) page and start rocking! Please Read the [FAQ](https://github.com/scribejava/scribejava/wiki/faq) before creating an issue :)

Also, remember to read the [fantastic tutorial](http://akoskm.github.io/2015/07/31/twitter-sign-in-for-web-apps.html) that [@akoskm](https://twitter.com/akoskm) wrote to easily integrate a server side app with an API (twitter in this case).

## Questions?

Feel free to drop us an email or create issue right here on github.com

## Forks

If you have a useful fork that should be listed there please contact us
