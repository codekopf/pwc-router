package com.codekopf.router.config;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import com.codekopf.router.model.Country;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class responsible for loading country data from the bundled JSON resource
 * and building an adjacency graph used for route calculation.
 */
@Slf4j
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
    public Map<String, Set<String>> adjacencyMap() {
        val countries = loadCountries();
        val adjacencyMap = buildAdjacencyMap(countries);
        return Collections.unmodifiableMap(adjacencyMap);
    }

    private List<Country> loadCountries() {
        try (val inputStream = new ClassPathResource(COUNTRIES_RESOURCE).getInputStream()) {
            val objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to load country data from classpath resource: " + COUNTRIES_RESOURCE, e);
        }
    }

    private Map<String, Set<String>> buildAdjacencyMap(final List<Country> countries) {
        val adjacencyMap = new HashMap<String, Set<String>>();

        for (var country : countries) {
            var countryCode = country.getCode();
            var countryNeighbours = country.getNeighbours();

            adjacencyMap.putIfAbsent(countryCode, new HashSet<>());

            if (countryNeighbours != null) {

                for (var neighbour : countryNeighbours) {
                    adjacencyMap.computeIfAbsent(countryCode, k -> new HashSet<>()).add(neighbour);
                    adjacencyMap.computeIfAbsent(neighbour, k -> new HashSet<>()).add(countryCode);
                }

            }
        }

        return adjacencyMap;
    }

}
