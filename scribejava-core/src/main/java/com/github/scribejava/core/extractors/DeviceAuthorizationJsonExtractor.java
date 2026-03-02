package com.github.scribejava.core.extractors;

import static com.github.scribejava.core.extractors.AbstractJsonExtractor.OBJECT_MAPPER;
import static com.github.scribejava.core.extractors.AbstractJsonExtractor.extractRequiredParameter;
import tools.jackson.databind.JsonNode;
import com.github.scribejava.core.model.DeviceAuthorization;
import java.io.IOException;
import com.github.scribejava.core.model.Response;

public class DeviceAuthorizationJsonExtractor extends AbstractJsonExtractor {

    protected DeviceAuthorizationJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final DeviceAuthorizationJsonExtractor INSTANCE = new DeviceAuthorizationJsonExtractor();
    }

    public static DeviceAuthorizationJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    public DeviceAuthorization extract(Response response) throws IOException {
        if (response.getCode() != 200) {
            generateError(response);
        }
        return createDeviceAuthorization(response.getBody());
    }

    public void generateError(Response response) throws IOException {
        OAuth2AccessTokenJsonExtractor.instance().generateError(response);
    }

    private DeviceAuthorization createDeviceAuthorization(String rawResponse) throws IOException {

        final JsonNode response = OBJECT_MAPPER.readTree(rawResponse);

        final DeviceAuthorization deviceAuthorization = new DeviceAuthorization(
                extractRequiredParameter(response, "device_code", rawResponse).asString(),
                extractRequiredParameter(response, "user_code", rawResponse).asString(),
                extractRequiredParameter(response, getVerificationUriParamName(), rawResponse).asString(),
                extractRequiredParameter(response, "expires_in", rawResponse).intValue());

        final JsonNode intervalSeconds = response.get("interval");
        if (intervalSeconds != null) {
            deviceAuthorization.setIntervalSeconds(intervalSeconds.asInt(5));
        }

        final JsonNode verificationUriComplete = response.get("verification_uri_complete");
        if (verificationUriComplete != null) {
            deviceAuthorization.setVerificationUriComplete(verificationUriComplete.asString());
        }

        return deviceAuthorization;
    }

    protected String getVerificationUriParamName() {
        return "verification_uri";
    }
}
