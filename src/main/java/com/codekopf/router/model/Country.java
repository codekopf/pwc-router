package com.codekopf.router.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a country entry from the countries JSON data source.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @JsonProperty("cca3")
    private String code;

    @JsonProperty("borders")
    private List<String> neighbours;

}
