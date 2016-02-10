package com.github.scribejava.core.model;

import java.io.Serializable;

/**
 * Represents an OAuth token (either request or access token) and its secret
 *
 * @author Pablo Fernandez
 */
public interface Token extends Serializable {
    String getToken();
    boolean isEmpty();
}
