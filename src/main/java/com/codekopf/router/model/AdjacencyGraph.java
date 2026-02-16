package com.codekopf.router.model;

import java.util.Map;
import java.util.Set;

/**
 * Wrapper type representing the country adjacency graph.
 * Each key is a cca3 country code, and the value is the set of its land-bordering country codes.
 */
public record AdjacencyGraph(Map<String, Set<String>> map) {

    /**
     * Checks whether the graph contains the given country code.
     *
     * @param code cca3 country code
     * @return true if the country code exists in the graph
     */
    public boolean containsCountry(final String code) {
        return map.containsKey(code);
    }

    /**
     * Returns the set of neighboring country codes for the given country.
     * Returns an empty set if the country has no neighbors or is not found.
     *
     * @param code cca3 country code
     * @return set of neighbouring cca3 country codes
     */
    public Set<String> getNeighbours(final String code) {
        return map.getOrDefault(code, Set.of());
    }

}
