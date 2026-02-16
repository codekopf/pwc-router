package com.codekopf.router.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.codekopf.router.exception.RouteNotFoundException;

/**
 * Service that calculates a land route between two countries using bidirectional BFS.
 * Bidirectional BFS explores from both the origin and destination simultaneously,
 * meeting in the middle for significantly better performance than standard BFS.
 */
@Service
public class RoutingService {

    private final Map<String, Set<String>> adjacencyMap;

    public RoutingService(final Map<String, Set<String>> adjacencyMap) {
        this.adjacencyMap = adjacencyMap;
    }

    /**
     * Finds a land route between two countries identified by their cca3 codes.
     * <p>
     * Fail fast, validating the input country codes before attempting to find a route.
     * If either code (origin and destination) is invalid, a RouteNotFoundException is thrown immediately
     * on origin code validation failure, without attempting to validate the destination code.
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

        return bidirectionalBfs(origin, destination);
    }

    private void validateCountryCode(final String code) {
        if (!adjacencyMap.containsKey(code)) {
            throw new RouteNotFoundException("Country code not found: " + code);
        }
    }

    /**
     * Performs bidirectional BFS from both origin and destination simultaneously.
     * The search alternates between expanding the smaller frontier, which ensures
     * optimal meeting in the middle and minimal node exploration.
     *
     * @param origin      starting country code
     * @param destination target country code
     * @return the shortest land route as an ordered list of country codes
     * @throws RouteNotFoundException if land does not connect the two countries
     */
    private List<String> bidirectionalBfs(final String origin, final String destination) {

        var originQueue = new LinkedList<String>(); // Queue for traveling from source toward destination
        var destinationQueue = new LinkedList<String>(); // Queue for traveling from destination toward source

        originQueue.add(origin);
        destinationQueue.add(destination);

        var parentsFromOriginDirection = new HashMap<String, String>(); // Track visited countries and their parents for the origin-side search
        var parentsFromDestinationDirection = new HashMap<String, String>(); // Track visited countries and their parents for the destination-side search

        parentsFromOriginDirection.put(origin, null);
        parentsFromDestinationDirection.put(destination, null);

        while (!originQueue.isEmpty() && !destinationQueue.isEmpty()) {

            final String meetingPoint;

            // Optimization - Expand the smaller queue first
            if (originQueue.size() <= destinationQueue.size()) {
                meetingPoint = expandFrontier(originQueue, parentsFromOriginDirection, parentsFromDestinationDirection);
            } else {
                meetingPoint = expandFrontier(destinationQueue, parentsFromDestinationDirection, parentsFromOriginDirection);
            }

            if (meetingPoint != null) {
                return reconstructPath(meetingPoint, parentsFromOriginDirection, parentsFromDestinationDirection);
            }
        }

        throw new RouteNotFoundException("No land route found from " + origin + " to " + destination);
    }

    /**
     * Expands one level of BFS from the given frontier queue.
     *
     * @param queue        the frontier queue to expand
     * @param thisParents  parent map for the current search direction
     * @param otherParents parent map for the opposite search direction
     * @return the meeting point country code if the two searches meet, or null
     */
    private String expandFrontier(final Queue<String> queue, final Map<String, String> thisParents, final Map<String, String> otherParents) {
        var levelSize = queue.size();

        for (int i = 0; i < levelSize; i++) {

            var currentCountry = queue.poll();

            var neighboringCountries = adjacencyMap.getOrDefault(currentCountry, Set.of());

            for (var neighbor : neighboringCountries) {

                if (thisParents.containsKey(neighbor)) {
                    continue;
                }

                thisParents.put(neighbor, currentCountry);
                queue.add(neighbor);

                if (otherParents.containsKey(neighbor)) {
                    return neighbor;
                }

            }
        }

        return null;
    }

    /**
     * Reconstructs the full path from origin to destination through the meeting point
     * by tracing parent pointers in both directions.
     *
     * @param meetingPoint  the country code where both searches met
     * @param forwardParents  parent map from origin-side BFS
     * @param backwardParents parent map from destination-side BFS
     * @return complete ordered route from origin to destination
     */
    private List<String> reconstructPath(final String meetingPoint, final Map<String, String> forwardParents, final Map<String, String> backwardParents) {
        var path = new ArrayList<String>();

        // Place the meeting point in the middle of the path
        var node = meetingPoint;

        // Trace from meeting point back to origin
        while (node != null) {
            path.add(node);
            node = forwardParents.get(node);
        }
        Collections.reverse(path);

        // Trace from meeting point forward to destination
        node = backwardParents.get(meetingPoint);
        while (node != null) {
            path.add(node);
            node = backwardParents.get(node);
        }

        return path;
    }

}
