package com.codekopf.router.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a country entry from the countries JSON data source.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @JsonProperty("cca3")
    private String code;

    @JsonProperty("borders")
    private List<String> neighbours;

    public Country() {
    }

    public String getCode() {
        return code;
    }

    public List<String> getNeighbours() {
        return neighbours;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setNeighbours(final List<String> neighbours) {
        this.neighbours = neighbours;
    }

}
