package com.codekopf.router.config;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import com.codekopf.router.model.Country;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class responsible for loading country data from the bundled JSON resource.
 */
@Slf4j
@Configuration
public class CountryDataConfig {

    private static final String COUNTRIES_RESOURCE = "countries.json";

    /**
     * Loads country data from the classpath resource.
     */
    @Bean
    public void adjacencyMap() {
        val countries = loadCountries();
    }

    private List<Country> loadCountries() {
        try (val inputStream = new ClassPathResource(COUNTRIES_RESOURCE).getInputStream()) {
            val objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to load country data from classpath resource: " + COUNTRIES_RESOURCE, e);
        }
    }

}
