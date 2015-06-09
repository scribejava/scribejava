package ru.hh.oauth.subscribe.core.model;

import ru.hh.oauth.subscribe.core.utils.Preconditions;

/**
 * Represents an OAuth verifier code.
 *
 * @author Pablo Fernandez
 */
public class Verifier {

    private final String value;

    /**
     * Default constructor.
     *
     * @param value verifier value
     */
    public Verifier(String value) {
        Preconditions.checkNotNull(value, "Must provide a valid string as verifier");
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
