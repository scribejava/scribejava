# Welcome to the home of O2Scribe , the simple OAuth2 Java lib!

This project started life as a fork of fernandezpablo85/scribe-java but due to the fragmentation of oauth2 as a result of the poor spec
the project has discontinued oauth2 support. This project aims to pick up where they left off. It is not intentionally compatible with scribe
and does not support oauth1 or oauth1a and has no intetion of doing so in the future.


![travis ci](https://secure.travis-ci.org/fernandezpablo85/scribe-java.png?branch=master)

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

### Supports 2.0 OAuth APIs out-of-the-box

* Facebook

* Foursquare

* and many more! check the [examples folder](http://github.com/set321go/scribe-java/tree/master/src/test/java/org/scribe/examples)

### Small and modular

Scribe's code is small (about 1k LOC) and simple to understand. No smart-ass or "clever" hacks here.

### Android-Ready

Works out of the box with android(TM) applications.

