package ru.hh.oauth.subscribe.model;

public abstract class SubScribeConfig {
    
    private static ForceTypeOfHttpRequest forceTypeOfHttpRequests = ForceTypeOfHttpRequest.NONE;

    public static ForceTypeOfHttpRequest getForceTypeRequest() {
        return forceTypeOfHttpRequests;
    }

    public static void setForceTypeRequest(ForceTypeOfHttpRequest forceTypeOfHttpRequests) {
        SubScribeConfig.forceTypeOfHttpRequests = forceTypeOfHttpRequests;
    }
}
