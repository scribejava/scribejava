# Welcome to the home of Scribe, the simple OAuth Java lib!

![travis ci](https://secure.travis-ci.org/fernandezpablo85/scribe-java.png?branch=master)

### Before submitting a pull request [please read this](https://github.com/fernandezpablo85/scribe-java/wiki/Scribe-scope-revised)

# Why use Scribe?

### Dead Simple

Who said OAuth was difficult? Configuring scribe is __so easy your grandma can do it__! check it out:

```java
OAuthService service = new ServiceBuilder()
                                  .provider(LinkedInApi.class)
                                  .apiKey(YOUR_API_KEY)
                                  .apiSecret(YOUR_API_SECRET)
                                  .build();
```

That **single line** (added newlines for readability) is the only thing you need to configure scribe with LinkedIn's OAuth API for example.

### Threadsafe

Hit Scribe as hard and with many threads as you like.

### Supports all major 1.0a and 2.0 OAuth APIs out-of-the-box

* Google

* Facebook

* Yahoo

* LinkedIn

* Twitter

* Foursquare

* Evernote

* Vimeo

* Yammer

* Windows Live

* and many more! check the [examples folder](http://github.com/fernandezpablo85/scribe-java/tree/master/src/test/java/org/scribe/examples)

### Small and modular

Scribe's code is small (about 1k LOC) and simple to understand. No smart-ass or "clever" hacks here.

### Android-Ready

Works out of the box with android(TM) applications.

### Stable & bulletproof

Good test coverage to keep you safe from harm.

When something bad actually happens, Scribe's meaningful error messages will tell you exactly what went wrong, when and where.

### Pull it from Maven!

You can pull scribe from my maven repository, just add these to your __pom.xml__ file:

```xml

<!-- repository -->
<repositories>
  <repository>
    <id>scribe-java-mvn-repo</id>
    <url>https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>

<!-- dependency -->
<dependency>
  <groupId>org.scribe</groupId>
  <artifactId>scribe</artifactId>
  <version>1.3.5</version>
</dependency>
```

## Getting started in less than 2 minutes

Check the [Getting Started](http://wiki.github.com/fernandezpablo85/scribe-java/getting-started) page and start rocking! Please Read the [FAQ](http://wiki.github.com/fernandezpablo85/scribe-java/faq) before creating an issue :)

## Questions?

Feel free to drop me an email, but there's already a [StackOverflow](http://stackoverflow.com) tag for [scribe](http://stackoverflow.com/questions/tagged/scribe) you should use. I'm subscribed to it so I'll pick the question immediately.

## Forks

Looking for a scribe variation? check the [Fork List](https://github.com/fernandezpablo85/scribe-java/wiki/Forks)

If you have a useful fork that should be listed there please contact me (see About me).

## About me

[LinkedIn profile](http://www.linkedin.com/in/fernandezpablo85)

Follow me: [@fernandezpablo](http://twitter.com/fernandezpablo)
