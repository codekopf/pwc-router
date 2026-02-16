package com.codekopf.router.dto;

import java.util.List;

/**
 * Response DTO containing the calculated land route as an ordered list of country codes (cca3).
 */
public record RouteResponse(List<String> route) {}