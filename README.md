# Welcome to the home of ScribeJava, the simple OAuth client Java lib!

[![Donate](https://www.paypalobjects.com/en_US/RU/i/btn/btn_donateCC_LG.gif)](https://github.com/scribejava/scribejava/blob/master/donate.md) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.scribejava/scribejava/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.scribejava/scribejava)


# Why use ScribeJava?

### Dead Simple

Who said OAuth/OAuth2 was difficult? Configuring ScribeJava is __so easy your grandma can do it__! check it out:

```java
OAuthService service = new ServiceBuilder(YOUR_CLIENT_ID)
                                  .apiSecret(YOUR_CLIENT_SECRET)
                                  .build(LinkedInApi20.instance());
```

That **single line** (added newlines for readability) is the only thing you need to configure ScribeJava with LinkedIn's OAuth API for example.

Working executable examples are [here](https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples)
Common usage: [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20Example.java)

### Threadsafe

Hit ScribeJava as hard and with many threads as you like.

### Java 7 compatible

That's it. You can use it in old environments and in android apps.
note: To compile from sources you will need Java 9 or newer

### Async and other HTTP clients

ScribeJava support out-of-box several HTTP clients:
 * ning async http client 1.9.x (maven module scribejava-httpclient-ning) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FacebookAsyncNingExample.java)
 * Async Http Client asynchttpclient 2.x (maven module scribejava-httpclient-ahc) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20AsyncAHCExample.java)
 * OkHttp (maven module scribejava-httpclient-okhttp) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/GitHubAsyncOkHttpExample.java)
 * Apache HttpComponents HttpClient (maven module scribejava-httpclient-apache) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FacebookAsyncApacheExample.java)
 * Armeria HTTP client (required >= java 8) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20ArmeriaExample.java)
 * any externally created HTTP client [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/VkontakteExternalHttpExample.java)

 just add corresponding maven modules to your pom

### Supports many flows and additional features

  * [RFC 6749](https://tools.ietf.org/html/rfc6749) The OAuth 2.0 Authorization Framework, [Authorization Code Authorization Grant](https://tools.ietf.org/html/rfc6749#section-4.1), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20Example.java)
  * [RFC 6749](https://tools.ietf.org/html/rfc6749) The OAuth 2.0 Authorization Framework, [Resource Owner Password Credentials Authorization Grant](https://tools.ietf.org/html/rfc6749#section-4.3)
  * [RFC 6749](https://tools.ietf.org/html/rfc6749) The OAuth 2.0 Authorization Framework, [Client Credentials Authorization Grant](https://tools.ietf.org/html/rfc6749#section-4.4), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/VkontakteClientCredentialsGrantExample.java)
  * [RFC 6749](https://tools.ietf.org/html/rfc6749) The OAuth 2.0 Authorization Framework, [Refreshing an Access Token](https://tools.ietf.org/html/rfc6749#section-6), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20Example.java#L77)
  * [RFC 6750](https://tools.ietf.org/html/rfc6750) The OAuth 2.0 Authorization Framework: Bearer Token Usage
  * [RFC 7636](https://tools.ietf.org/html/rfc7636) Proof Key for Code Exchange by OAuth Public Clients (PKCE), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20WithPKCEExample.java)
  * [RFC 7009](https://tools.ietf.org/html/rfc7009) OAuth 2.0 Token Revocation, [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20RevokeExample.java)
  * [RFC 8628](https://tools.ietf.org/html/rfc8628) OAuth 2.0 Device Authorization Grant [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20DeviceAuthorizationGrantExample.java)
  * [RFC 5849](https://tools.ietf.org/html/rfc5849) The OAuth 1.0 Protocol, [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/TwitterExample.java)

### Supports all (50+) major 1.0a and 2.0 OAuth APIs out-of-the-box

* Asana (https://asana.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/AsanaExample.java)
* Automatic (https://www.automatic.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/AutomaticExample.java)
* AWeber (http://www.aweber.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/AWeberExample.java)
* Box (https://www.box.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Box20Example.java)
* Dataporten (https://docs.dataporten.no/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/DataportenExample.java)
* Digg (http://digg.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/DiggExample.java)
* Discord (https://discordapp.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/DiscordExample.java)
* Доктор на работе (https://www.doktornarabote.ru/)
* Dropbox (https://www.dropbox.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/DropboxExample.java)
* Etsy (https://www.etsy.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/EtsyExample.java)
* Facebook (https://www.facebook.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FacebookExample.java), [example with Async Apache HTTP client](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FacebookAsyncApacheExample.java), [example with Async Ning HTTP client](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FacebookAsyncNingExample.java)
* Fitbit (https://www.fitbit.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FitbitApi20Example.java)
* Flickr (https://www.flickr.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FlickrExample.java)
* Foursquare (https://foursquare.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Foursquare2Example.java), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FoursquareExample.java)
* Frappe (https://github.com/frappe/frappe) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FrappeExample.java)
* Freelancer (https://www.freelancer.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/FreelancerExample.java)
* Genius (http://genius.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/GeniusExample.java)
* GitHub (https://github.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/GitHubExample.java), [example with OkHttp HTTP client](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/GitHubAsyncOkHttpExample.java)
* Google (https://www.google.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20Example.java), [example with Async Http Client](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20AsyncAHCExample.java), [example Revoke](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20RevokeExample.java), [example with PKCEE](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20WithPKCEExample.java)
* HeadHunter ХэдХантер (https://hh.ru/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/HHExample.java)
* HiOrg-Server (https://www.hiorg-server.de/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/HiOrgServerExample.java)
* Imgur (http://imgur.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/ImgurExample.java)
* Instagram (https://www.instagram.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/InstagramExample.java)
* Kaixin 开心网 (http://www.kaixin001.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Kaixin20Example.java)
* Kakao (https://kakao.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/KakaoExample.java)
* Keycloak (https://www.keycloak.org/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/KeycloakExample.java)
* LinkedIn (https://www.linkedin.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/LinkedIn20Example.java), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/LinkedInExample.java), [example with custom scopes](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/LinkedInExampleWithScopes.java)
* Mail.Ru (https://mail.ru/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/MailruExample.java), [example with Async Ning HTTP Client](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/MailruAsyncExample.java)
* MediaWiki (https://www.mediawiki.org/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/MediaWikiExample.java)
* Meetup (https://www.meetup.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Meetup20Example.java), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/MeetupExample.java)
* Microsoft Azure Active Directory (Azure AD) (http://azure.microsoft.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/MicrosoftAzureActiveDirectoryExample.java)
* Microsoft Azure Active Directory (Azure AD) 2.0 (http://azure.microsoft.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/MicrosoftAzureActiveDirectory20Example.java)
* Microsoft Live (https://login.live.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/LiveExample.java)
* Misfit (http://misfit.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/MisfitExample.java)
* NAVER (http://www.naver.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/NaverExample.java)
* NetSuite (https://www.netsuite.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/NetSuiteExample.java)
* Odnoklassniki Одноклассники (http://ok.ru/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/OdnoklassnikiExample.java)
* Polar (https://www.polar.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/PolarAPIExample.java)
* Pinterest (https://www.pinterest.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/PinterestExample.java)
* 500px (https://500px.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Px500Example.java)
* Renren (http://renren.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/RenrenExample.java)
* Salesforce (https://www.salesforce.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/SalesforceExample.java), [example with Async Ning HTTP Client](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/SalesforceNingAsyncExample.java)
* Sina (http://www.sina.com.cn/ http://weibo.com/login.php) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/SinaWeibo2Example.java), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/SinaWeiboExample.java)
* Skyrock (http://skyrock.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/SkyrockExample.java)
* Slack (https://slack.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/SlackExample.java)
* StackExchange (http://stackexchange.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/StackExchangeExample.java)
* The Things Network (v1-staging and v2-preview) (https://www.thethingsnetwork.org/) [example v1](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/TheThingsNetworkV1StagingExample.java), [example v2 preview](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/TheThingsNetworkV2PreviewExample.java)
* Trello (https://trello.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/TrelloExample.java)
* Tumblr (https://www.tumblr.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/TumblrExample.java)
* TUT.BY (http://www.tut.by/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/TutByExample.java)
* Twitter (https://twitter.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/TwitterExample.java)
* uCoz (https://www.ucoz.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/UcozExample.java)
* Viadeo (http://viadeo.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/ViadeoExample.java)
* VK ВКонтакте (http://vk.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/VkontakteExample.java), [example Client Credentials Grant](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/VkontakteClientCredentialsGrantExample.java), [example with External HTTP Client](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/VkontakteExternalHttpExample.java)
* Wunderlist (https://www.wunderlist.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/WunderlistExample.java)
* Xero (https://www.xero.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/XeroExample.java)
* XING (https://www.xing.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/XingExample.java)
* Yahoo (https://www.yahoo.com/) [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Yahoo20Example.java), [example](https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/YahooExample.java)
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
    <version>8.3.3</version>
</dependency>
```

And in case you need just core classes (that's it, without any external API (FB, VK, GitHub, Google etc) specific code), you could pull just 'core' artifact.
```xml
<dependency>
    <groupId>com.github.scribejava</groupId>
    <artifactId>scribejava-core</artifactId>
    <version>8.3.3</version>
</dependency>
```

## How can I help ScribeJava

First of all, Pull Requests are welcome, the second option is [donations](https://github.com/scribejava/scribejava/blob/master/donate.md).

## When will ScribeJava support XXX (new RFC, custom functionality, new API etc.)

When you will send the pull request. That's the way for a majority of changes here.
Or you can ask someone to make the paid job for you.
In some cases, when I'm interested in changes (technically or financially), I can implement the request myself.

## Paid consulting
If you or your business depends on the Scribejava and you need any specific improvement or new feature not currently implemented in the Scribejava, consider contacting me about a paid job.

## Getting started in less than 2 minutes

Check the [Getting Started](https://github.com/scribejava/scribejava/wiki/getting-started) page and start rocking! Please Read the [FAQ](https://github.com/scribejava/scribejava/wiki/faq) before creating an issue :)

Some useful info and answers you can find on the [wiki](https://github.com/scribejava/scribejava/wiki)

Also, remember to read the [fantastic tutorial](http://akoskm.github.io/2015/07/31/twitter-sign-in-for-web-apps.html) that [@akoskm](https://twitter.com/akoskm) wrote to easily integrate a server side app with an API (twitter in this case).

## Questions?

Feel free to drop us an email or create issue right here on github.com

## Forks

If you have a useful fork that should be listed there please contact us
