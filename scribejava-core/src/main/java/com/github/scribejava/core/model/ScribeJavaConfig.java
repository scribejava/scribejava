package com.github.scribejava.core.model;

public abstract class ScribeJavaConfig {

    private static ForceTypeOfHttpRequest forceTypeOfHttpRequests = ForceTypeOfHttpRequest.NONE;

    public static ForceTypeOfHttpRequest getForceTypeOfHttpRequests() {
        return forceTypeOfHttpRequests;
    }

    public static void setForceTypeOfHttpRequests(ForceTypeOfHttpRequest forceTypeOfHttpRequests) {
        ScribeJavaConfig.forceTypeOfHttpRequests = forceTypeOfHttpRequests;
    }
}
