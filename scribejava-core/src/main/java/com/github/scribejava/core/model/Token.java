package com.github.scribejava.core.model;

import java.io.Serializable;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Represents an OAuth token (either request or access token) and its secret
 *
 * @author Pablo Fernandez
 */
public interface Token extends Serializable {
    public String getToken();
    public boolean isEmpty();
}
