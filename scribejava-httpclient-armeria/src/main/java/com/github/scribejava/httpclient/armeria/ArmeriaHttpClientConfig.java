package com.github.scribejava.httpclient.armeria;

import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.linecorp.armeria.client.ClientFactoryOption;
import com.linecorp.armeria.client.ClientFactoryOptionValue;
import com.linecorp.armeria.client.ClientOption;
import com.linecorp.armeria.client.ClientOptions;
import com.linecorp.armeria.client.ClientOptionsBuilder;
import com.linecorp.armeria.common.SessionProtocol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArmeriaHttpClientConfig implements HttpClientConfig {

  private Map<String, Object> options;
  private Map<String, Object> factoryOptions;
  private String protocolPreference;
  private Boolean logging;

  ClientOptions getClientOptions() {
    if (options == null || options.isEmpty()) {
      return null;
    }
    final ClientOptionsBuilder builder = ClientOptions.builder();
    for (Map.Entry<String, Object> optionEntry : options.entrySet()) {
      builder.option(ClientOption.valueOf(optionEntry.getKey()), optionEntry.getValue());
    }
    return builder.build();
  }

  public Map<String, Object> getOptions() {
    return options;
  }

  public void setOptions(Map<String, Object> options) {
    this.options = options;
  }

  Collection<ClientFactoryOptionValue<?>> getClientFactoryOptions() {
    if (factoryOptions == null || factoryOptions.isEmpty()) {
      return Collections.emptyList();
    }
    final List<ClientFactoryOptionValue<?>> options = new ArrayList<>();
    for (Map.Entry<String, Object> optionEntry : factoryOptions.entrySet()) {
      options.add(ClientFactoryOption.valueOf(optionEntry.getKey()).newValue(optionEntry.getValue()));
    }
    // WARNING: this ClientFactoryOptions.of(options) method will override default channel options of the factory
    // and cause NPE
    //return ClientFactoryOptions.of(options);
    return options;
  }

  public Map<String, Object> getFactoryOptions() {
    return factoryOptions;
  }

  public void setFactoryOptions(Map<String, Object> factoryOptions) {
    this.factoryOptions = factoryOptions;
  }

  SessionProtocol getSessionProtocolPreference() {
    if (protocolPreference == null) {
      return SessionProtocol.H1; // default
    }
    final SessionProtocol protocol = SessionProtocol.of(protocolPreference);
    switch(protocol) {
      case H1:
      case H1C:
        return SessionProtocol.H1;
      case PROXY:
        throw new IllegalArgumentException("protocolPreference - " + SessionProtocol.PROXY.uriText());
      default:
        return SessionProtocol.H2;
    }
  }

  public String getProtocolPreference() {
    return protocolPreference;
  }

  public void setProtocolPreference(String protocolPreference) {
    this.protocolPreference = protocolPreference;
  }

  public boolean isLogging() {
    return logging != null && logging;
  }

  public void setLogging(boolean logging) {
    this.logging = logging;
  }

  @Override
  public HttpClientConfig createDefaultConfig() {
    return defaultConfig();
  }

  public static ArmeriaHttpClientConfig defaultConfig() {
    return new ArmeriaHttpClientConfig();
  }
}
