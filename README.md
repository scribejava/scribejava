# Welcome to the home of SubScribe, the complete OAuth Java lib!
originaly forked from Scribe (https://github.com/fernandezpablo85/scribe-java)

## Main reasons of fork here:
1. https://github.com/fernandezpablo85/scribe-java/wiki/Scribe-scope-revised
2. We really think, OAuth2.0 should be here;
3. We really think, async http should be here for a high-load projects;
4. We really think, all APIs should be here. With all their specific stuff. It's easier to change/fix/add API here,
in this lib, one time, instead of N programmers will do the same things on their sides;
5. Scribe should be multi-maven-module project. Core and APIs should be deployed as separated artifacts.

### So, while Scribe is going to be "a library that makes OAuth request signing dead simple.", SubScribe is going to be "a library that makes OAuth requests to any OAuth Provider dead simple."

### For the moment SubScribe additionally supports Odnoklassniki, Mail.ru,  LinkedIn2.0, Google2.0..
For the full list check the [examples folder](https://github.com/hhru/subscribe/tree/master/src/test/java/org/scribe/examples)

# Pull it from Maven!

You can pull scribe from my maven repository, just add these to your __pom.xml__ file:

```xml
<!-- dependency -->
<dependency>
  <groupId>ru.hh.oauth.subscribe</groupId>
  <artifactId>subscribe</artifactId>
</dependency>
```

# About Us

The most high-load job-site in Eastern Europe.

Our hosts are hh.ru/hh.ua/hh.kz/jobs.tut.by/rabota.mail.ru/jobs.day.az/career.ru
