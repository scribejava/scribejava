package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

/**
 * OAuth2 API for HiOrg-Server
 *
 * @see https://wiki.hiorg-server.de/admin/oauth2
 */
public class HiOrgServerApi20 extends DefaultApi20 {

    private final String version;

    protected HiOrgServerApi20() {
        this("v1");
    }

    protected HiOrgServerApi20(String version) {
        this.version = version;
    }

    private static class InstanceHolder {

        private static final HiOrgServerApi20 INSTANCE = new HiOrgServerApi20();
    }

    public static HiOrgServerApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    public static HiOrgServerApi20 customVersion(String version) {
        return new HiOrgServerApi20(version);
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.hiorg-server.de/api/oauth2/" + version + "/token.php";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.hiorg-server.de/api/oauth2/" + version + "/authorize.php";
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

}
