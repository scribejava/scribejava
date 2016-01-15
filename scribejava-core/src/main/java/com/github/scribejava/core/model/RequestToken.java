package com.github.scribejava.core.model;

import com.github.scribejava.core.utils.Preconditions;

/**
 *
 * @author Daniel Tyreus
 */
public interface RequestToken extends Token{
    public String getSecret();
}
