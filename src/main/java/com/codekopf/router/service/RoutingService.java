package com.codekopf.router.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.codekopf.router.exception.RouteNotFoundException;

/**
 * Service that calculates a land route between two countries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoutingService {

    private final Map<String, Set<String>> adjacencyMap;

    /**
     * Finds a land route between two countries identified by their cca3 codes.
     * <p>
     * Fail fast, validating the input country codes before attempting to find a route.
     * If either code (origin and destination) is invalid, a RouteNotFoundException is thrown immediately
     * on source code validation failure, without attempting to validate the destination code.
     *
     * @param origin      the cca3 code of the origin country
     * @param destination the cca3 code of the destination country
     * @return ordered a list of cca3 country codes representing the route
     * @throws RouteNotFoundException if no land route exists or country codes are invalid
     */
    public List<String> findRoute(final String origin, final String destination) {

        // Validate input country codes before attempting to find a route
        validateCountryCode(origin);
        validateCountryCode(destination);

        // Edge case: if origin and destination are the same, return a route with just that country
        if (origin.equals(destination)) {
            return List.of(origin);
        }

        return List.of(origin, destination);
    }

    private void validateCountryCode(final String code) {
        if (!adjacencyMap.containsKey(code)) {
            throw new RouteNotFoundException("Country code not found: " + code);
        }
    }

}
