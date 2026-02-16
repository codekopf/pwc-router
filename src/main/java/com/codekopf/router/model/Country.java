package com.codekopf.router.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a country entry from the countries JSON data source.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Country(
    @JsonProperty("cca3") String code,
    @JsonProperty("borders") List<String> neighbours
) {}