package com.codekopf.router.exception;

/**
 * Thrown when no land route can be found between the specified origin and destination countries.
 */
public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(final String message) {
        super(message);
    }

}
