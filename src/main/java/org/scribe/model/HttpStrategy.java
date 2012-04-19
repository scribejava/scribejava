package org.scribe.model;

/** 
 * Allows the Request class to use a pluggable strategy for making 
 * the actual HTTP Connection.
 *   
 * @author mkishor
 */
public interface HttpStrategy {
    public Response send(Request request);
}
