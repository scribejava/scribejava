package com.github.scribejava.httpclient.armeria;

import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.ClientOptions;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.client.retry.Backoff;
import com.linecorp.armeria.client.retry.RetryRule;
import com.linecorp.armeria.client.retry.RetryingClient;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.logging.LogLevel;
import java.util.function.Function;
import org.slf4j.LoggerFactory;

public class ArmeriaHttpClientConfig implements HttpClientConfig {

  private static final SessionProtocol DEFAULT_PROTOCOL_PREFERENCE = SessionProtocol.H1; // H1 or H2

  private final ClientOptions clientOptions;
  private final ClientFactory clientFactory;
  private SessionProtocol protocolPreference;
  private Function<? super HttpClient, RetryingClient> retry;
  private Function<? super HttpClient, LoggingClient> logging;

  /**
   * Creates new {@link ArmeriaHttpClientConfig} using provided {@link ClientOptions} and
   * {@link ClientFactory}.
   */
  public ArmeriaHttpClientConfig(ClientOptions clientOptions, ClientFactory clientFactory) {
    this.clientOptions = clientOptions;
    this.clientFactory = clientFactory;
    protocolPreference = DEFAULT_PROTOCOL_PREFERENCE;
  }

  /**
   * Creates new {@link ArmeriaHttpClientConfig} using default settings.
   */
  ArmeriaHttpClientConfig() {
    this(null, null);
  }

  /**
   * Creates new {@link HttpClientConfig} using default settings.
   */
  @Override
  public HttpClientConfig createDefaultConfig() {
    return defaultConfig();
  }

  /**
   * Creates new {@link ArmeriaHttpClientConfig} using default settings.
   */
  public static ArmeriaHttpClientConfig defaultConfig() {
    return new ArmeriaHttpClientConfig();
  }

  /**
   * Selects which protocol shall take preference when generic protocol scheme used by the URL,
   * like {@code http} or {@code https}.
   *
   * @param protocolPreference specifies which protocol shall take preference.
   *                           Acceptable values: {@code H1}, {@code HTTP1}, {@code HTTP/1.1} for
   *                           {@code HTTP/1.1} and {@code H2}, {@code HTTP2}, {@code HTTP/2} for
   *                           {@code HTTP/2}.
   */
  public void protocolPreference(String protocolPreference) {
    switch (protocolPreference.toUpperCase()) {
      case "H1":
      case "HTTP1":
      case "HTTP/1.1":
        this.protocolPreference = SessionProtocol.H1;
        break;
      case "H2":
      case "HTTP2":
      case "HTTP/2":
        this.protocolPreference = SessionProtocol.H2;
        break;
      default:
        throw new IllegalArgumentException("Invalid protocolPreference: " + protocolPreference);
    }
  }

  /**
   * Selects which protocol shall take preference when generic protocol scheme used by the URL,
   * like {@code http} or {@code https}.
   *
   * @param protocolPreference specifies which protocol shall take preference.
   *                           Acceptable values: {@link SessionProtocol#H1} and
   *                           {@link SessionProtocol#H2}
   */
  public void protocolPreference(SessionProtocol protocolPreference) {
    switch (protocolPreference) {
      case H1:
        this.protocolPreference = SessionProtocol.H1;
        break;
      case H2:
        this.protocolPreference = SessionProtocol.H2;
        break;
      default:
        throw new IllegalArgumentException("Invalid protocolPreference: " + protocolPreference);
    }
  }

  /**
   * Sets the client to retry when the remote end-point responds with one of the
   * specified {@code statuses}.
   *
   * Uses a backoff that computes a delay using one of supported functions, such as
   * {@code exponential(long, long, double)}, {@code fibonacci(long, long)}, {@code fixed(long)}
   * and {@code random(long, long)} chaining with {@code withJitter(double, double)} and
   * {@code withMaxAttempts(int)} from the {@code backoff} string that conforms to
   * the following format:
   * <ul>
   *   <li>{@code exponential=[initialDelayMillis:maxDelayMillis:multiplier]} is for
   *       {@code exponential(long, long, double)} (multiplier will be 2.0 if it's omitted)</li>
   *   <li>{@code fibonacci=[initialDelayMillis:maxDelayMillis]} is for
   *       {@code fibonacci(long, long)}</li>
   *   <li>{@code fixed=[delayMillis]} is for {@code fixed(long)}</li>
   *   <li>{@code random=[minDelayMillis:maxDelayMillis]} is for {@code random(long, long)}</li>
   *   <li>{@code jitter=[minJitterRate:maxJitterRate]} is for {@code withJitter(double, double)}
   *       (if only one jitter value is specified, it will be used for {@code withJitter(double)}</li>
   *   <li>{@code maxAttempts=[maxAttempts]} is for {@code withMaxAttempts(int)}</li>
   * </ul>
   * The order of options does not matter, and the {@code backoff} needs at least one option.
   * If you don't specify the base option exponential backoff will be used. If you only specify
   * a base option, jitter and maxAttempts will be set by default values. For example:
   * <ul>
   *   <li>{@code exponential=200:10000:2.0,jitter=0.2} (default)</li>
   *   <li>{@code exponential=200:10000,jitter=0.2,maxAttempts=50} (multiplier omitted)</li>
   *   <li>{@code fibonacci=200:10000,jitter=0.2,maxAttempts=50}</li>
   *   <li>{@code fixed=100,jitter=-0.5:0.2,maxAttempts=10} (fixed backoff with jitter variation)</li>
   *   <li>{@code random=200:1000} (jitter and maxAttempts will be set by default values)</li>
   * </ul>
   *
   * @param backoff the specification used to create a retry backoff
   * @param statuses the list of HTTP statuses on which to retry
   */
  public void retry(String backoff, HttpStatus... statuses) {
    final Backoff retryBackoff = Backoff.of(backoff);
    final RetryRule retryRule =
        RetryRule.builder().onStatus(statuses).onUnprocessed().thenBackoff(retryBackoff);
    retry = RetryingClient.newDecorator(retryRule);
  }

  /**
   * Sets the client to log requests and responses to the specified {@code logger}.
   * This method explicitly specifies various log levels. The log level correspond to a value
   * of {@link LogLevel} and must use one of the following values:
   * {@code ["OFF", "TRACE", "DEBUG", "INFO", "WARN", "ERROR"]}
   *
   * @param logger the logger name (of {@link org.slf4j.Logger}) to log requests/responses to
   * @param requestLevel the log level to use for logging requests, default {@code "DEBUG"}
   * @param responseLevel the log level to use for logging responses, default {@code "DEBUG"}
   * @param failureResponseLevel the log level to use for logging error responses,
   *                             default {@code "WARN"}
   */
  public void logging(String logger, String requestLevel, String responseLevel,
      String failureResponseLevel) {
    this.logging = LoggingClient.builder()
        .logger(LoggerFactory.getLogger(logger))
        .requestLogLevel(LogLevel.valueOf(requestLevel))
        .successfulResponseLogLevel(LogLevel.valueOf(responseLevel))
        .failureResponseLogLevel(LogLevel.valueOf(failureResponseLevel))
        .newDecorator();
  }

  /**
   * Sets the client to log requests and responses to the specified {@code logger} using default
   * log levels.
   *
   * @param logger the logger name (of {@link org.slf4j.Logger}) to log requests/responses to
   */
  public void logging(String logger) {
    this.logging = LoggingClient.builder()
        .logger(LoggerFactory.getLogger(logger))
        .requestLogLevel(LogLevel.DEBUG)
        .successfulResponseLogLevel(LogLevel.DEBUG)
        .failureResponseLogLevel(LogLevel.WARN)
        .newDecorator();
  }

  /**
   * Sets the client to log requests and responses to a default logger using default log levels.
   */
  public void logging() {
    this.logging = LoggingClient.builder()
        .requestLogLevel(LogLevel.DEBUG)
        .successfulResponseLogLevel(LogLevel.DEBUG)
        .failureResponseLogLevel(LogLevel.WARN)
        .newDecorator();
  }

  ArmeriaWebClientBuilder builder() {
    return new ArmeriaWebClientBuilder(clientOptions, clientFactory, protocolPreference,
        retry, logging);
  }
}
