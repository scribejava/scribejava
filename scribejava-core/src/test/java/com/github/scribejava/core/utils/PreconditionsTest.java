package com.github.scribejava.core.utils;

import org.junit.Test;

public class PreconditionsTest {

    private static final String ERROR_MSG = "";

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullObjects() {
        Preconditions.checkNotNull(null, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullStrings() {
        Preconditions.checkEmptyString(null, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForEmptyStrings() {
        Preconditions.checkEmptyString("", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForSpacesOnlyStrings() {
        Preconditions.checkEmptyString("               ", ERROR_MSG);
    }
}
