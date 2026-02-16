package com.codekopf.router.config;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.codekopf.router.model.Country;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class responsible for loading country data from the bundled JSON resource
 * and building an adjacency graph used for route calculation.
 */
@Configuration
public class CountryDataConfig {

    private static final String COUNTRIES_RESOURCE = "countries.json";

    /**
     * Loads country data from the classpath resource and builds an adjacency map
     * where each country code maps to the set of its land-bordering country codes.
     *
     * Naming note: The adjacency map is actually a list of sets with bidirectional relationships between countries,
     *              but "adjacency map" is a more concise and commonly understood term in graph theory contexts.
     *
     * @return unmodifiable adjacency map where the key is cca3 country codes and value is a set of neighboring cca3 country codes
     */
    @Bean
    @Qualifier("adjacencyMap")
    public Map<String, Set<String>> adjacencyMap() {
        var countries = loadCountries();
        var adjacencyMap = buildAdjacencyMap(countries);
        return Collections.unmodifiableMap(adjacencyMap);
    }

    private List<Country> loadCountries() {
        try (var inputStream = new ClassPathResource(COUNTRIES_RESOURCE).getInputStream()) {
            var objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to load country data from classpath resource: " + COUNTRIES_RESOURCE, e);
        }
    }

    private Map<String, Set<String>> buildAdjacencyMap(final List<Country> countries) {
        var adjacencyMap = new HashMap<String, Set<String>>();
        for (var country : countries) {
            if (country.neighbours() == null) {
                continue;
            }
            country.neighbours().forEach(neighbour -> {
                adjacencyMap.computeIfAbsent(country.code(), k -> new HashSet<>()).add(neighbour);
                adjacencyMap.computeIfAbsent(neighbour, k -> new HashSet<>()).add(country.code());
            });
        }
        return adjacencyMap;
    }

}
