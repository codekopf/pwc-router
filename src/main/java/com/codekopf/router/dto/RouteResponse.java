package com.codekopf.router.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO containing the calculated land route as an ordered list of country codes (cca3).
 */
@Getter
@AllArgsConstructor
public class RouteResponse {

    private final List<String> route;

}
