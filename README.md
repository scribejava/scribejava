# Welcome to the home of ScribeJava, the simple OAuth Java lib!

# Why use ScribeJava?

### Dead Simple

Who said OAuth/OAuth2 was difficult? Configuring scribe is __so easy your grandma can do it__! check it out:

```java
OAuthService service = new ServiceBuilder()
                                  .provider(LinkedInApi.class)
                                  .apiKey(YOUR_API_KEY)
                                  .apiSecret(YOUR_API_SECRET)
                                  .build();
```

That **single line** (added newlines for readability) is the only thing you need to configure scribe with LinkedIn's OAuth API for example.

### Threadsafe

Hit ScribeJava as hard and with many threads as you like.

### Async

You can user ning async http client out-of-box, just use ServiceBuilderAsync

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

* and many more! check the [examples folder](http://github.com/scribejava/scribejava/tree/master/src/test/java/com/github/scribejava/apis/examples)

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
    <version>2.0</version>
</dependency>
```

And in case you need just core classes (that's it, without any external API (FB, VK, GitHub, Google etc) specific code), you could pull just 'core' artifact.
```xml
<dependency>
    <groupId>com.github.scribejava</groupId>
    <artifactId>scribejava-core</artifactId>
    <version>2.0</version>
</dependency>
```

## Getting started in less than 2 minutes

Check the [Getting Started](https://github.com/scribejava/scribejava/wiki/getting-started) page and start rocking! Please Read the [FAQ](https://github.com/scribejava/scribejava/wiki/faq) before creating an issue :)

Also, remember to read the [fantastic tutorial](http://akoskm.github.io/2015/07/31/twitter-sign-in-for-web-apps.html) that [@akoskm](https://twitter.com/akoskm) wrote to easily integrate a server side app with an API (twitter in this case).

## Questions?

Feel free to drop us an email or create issue right here on github.com

## Forks

Looking for a ScribeJava variation? check the [Fork List](https://github.com/scribejava/scribejava/wiki/Forks)

If you have a useful fork that should be listed there please contact us
