package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class MediaWikiApi extends DefaultApi10a {

    private static final MediaWikiApi WIKIMEDIA_INSTANCE = new MediaWikiApi(
        "https://meta.wikimedia.org/w/index.php",
        "https://meta.wikimedia.org/wiki/"
    );

    private static final MediaWikiApi WIKIMEDIA_BETA_INSTANCE = new MediaWikiApi(
        "https://meta.wikimedia.beta.wmflabs.org/w/index.php",
        "https://meta.wikimedia.beta.wmflabs.org/wiki/"
    );

    private final String indexUrl;
    private final String niceUrlBase;

    /**
     * @param indexUrl The URL to the index.php of the wiki.
     * Due to <a href="https://phabricator.wikimedia.org/T59500">a MediaWiki bug</a>,
     * some requests must currently use the non-nice URL.
     * @param niceUrlBase The base of nice URLs for the wiki, including the trailing slash.
     * Due to <a href="https://phabricator.wikimedia.org/T74186">another MediaWiki bug</a>,
     * some requests must currently use the nice URL.
     */
    public MediaWikiApi(final String indexUrl, final String niceUrlBase) {
        this.indexUrl = indexUrl;
        this.niceUrlBase = niceUrlBase;
    }

    /**
     * The instance for wikis hosted by the Wikimedia Foundation.
     * Consumers are requested on
     * <a href="https://meta.wikimedia.org/wiki/Special:OAuthConsumerRegistration/propose">
     * Special:OAuthConsumerRegistration/propose
     * </a>.
     */
    public static MediaWikiApi wikimediaInstance() {
        return WIKIMEDIA_INSTANCE;
    }

    /**
     * The instance for wikis in the Wikimedia Foundation’s Beta Cluster.
     * Consumers are requested on
     * <a href="https://meta.wikimedia.beta.wmflabs.org/wiki/Special:OAuthConsumerRegistration/propose">
     * Special:OAuthConsumerRegistration/propose
     * </a>.
     */
    public static MediaWikiApi wikimediaBetaInstance() {
        return WIKIMEDIA_BETA_INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return indexUrl + "?title=Special:OAuth/initiate";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return niceUrlBase + "Special:OAuth/authorize";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return indexUrl + "?title=Special:OAuth/token";
    }

}
