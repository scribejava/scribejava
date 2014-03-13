package org.scribe.builder.authUrl;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 *
 */
public class DefaultAuthUrlBuilder implements AuthUrlBuilder {
    private static final char INTIAL_PARAMETER_IDENTIFIER = '?';
    private static final String CLIENT_ID_PARAM = "client_id=";
    private static final String PARAMETETER_IDENTIFIER = "&";
    private static final String REDIRECT_URI_PARAM = "redirect_uri=";
    private static final String SCOPE_PARAM = "scope=";
    private static final String STATE_PARAM = "state=";
    private static final String RESPONSE_TYPE_PARAM = "response_type";

    private String endpoint;
    private String clientId;
    private String redirectUrl;
    private String scope;
    private String state;
    private String responseType;

    @Override
    public AuthUrlBuilder setEndpoint(final String endpoint) {
        Preconditions.checkValidUrl(endpoint, "The endpoint should be a vaild url");
        this.endpoint = endpoint;
        return this;
    }

    @Override
    public AuthUrlBuilder setClientId(final String clientId) {
        Preconditions.checkEmptyString(clientId, "Client id is required");
        this.clientId = clientId;
        return this;
    }

    @Override
    public AuthUrlBuilder setRedirectUrl(final String redirectUrl) {
        Preconditions.checkValidUrl(redirectUrl, "The redirect url should be a valid url");
        this.redirectUrl = redirectUrl;
        return this;
    }

    @Override
    public AuthUrlBuilder setScope(final String scope) {
        this.scope = scope;
        return this;
    }

    @Override
    public AuthUrlBuilder setState(final String state) {
        this.state = state;
        return null;
    }

    @Override
    public AuthUrlBuilder setResponseType(final String responseType) {
        this.responseType = responseType;
        return this;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append(endpoint)
                .append(INTIAL_PARAMETER_IDENTIFIER)
                .append(CLIENT_ID_PARAM)
                .append(clientId);

        addParameter(sb, REDIRECT_URI_PARAM, OAuthEncoder.encode(redirectUrl));

        addOptionalParameter(sb, SCOPE_PARAM, scope);
        addOptionalParameter(sb, STATE_PARAM, state);
        addOptionalParameter(sb, RESPONSE_TYPE_PARAM, responseType);

        return sb.toString();
    }

    protected void addOptionalParameter(final StringBuilder sb, final String paramName, final String paramValue) {
        if (paramValue != null && !paramValue.isEmpty()) {
            addParameter(sb, paramName, paramValue);
        }
    }

    protected void addParameter(final StringBuilder sb, final String paramName, final String paramValue) {
        sb.append(PARAMETETER_IDENTIFIER)
                .append(paramName)
                .append(paramValue);
    }


}
