package com.codekopf.router.service;

import java.util.List;
import java.util.stream.Stream;

import com.codekopf.router.exception.RouteNotFoundException;
import com.codekopf.router.utils.AdjacencyGraphBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoutingServiceTest {

    private RoutingService routingService;

    @BeforeEach
    void setUp() {
        var adjacencyGraph = AdjacencyGraphBuilder.getSimpleAdjacencyGraphForTesting();
        routingService = new RoutingService(adjacencyGraph);
    }

    @Test
    void shouldFindDirectRoute() {
        var route = routingService.findRoute("CZE", "AUT");
        assertEquals(List.of("CZE", "AUT"), route);
    }

    @Test
    void shouldFindRouteViaIntermediateCountry() {
        var route = routingService.findRoute("CZE", "ITA");
        assertEquals(3, route.size());
        assertEquals("CZE", route.getFirst());
        assertEquals("AUT", route.get(1));
        assertEquals("ITA", route.getLast());
    }

    @Test
    void shouldFindShortestRouteIfMultipleOfSameLengthArePossible() {
        var route = routingService.findRoute("CZE", "ESP");
        assertEquals(4, route.size());
        assertEquals("CZE", route.getFirst());
        assertEquals("ESP", route.getLast());
    }

    @Test
    void shouldFindShortestRoute() {
        var route = routingService.findRoute("KAZ", "AFG");
        assertEquals(3, route.size());
        assertEquals("KAZ", route.getFirst());
        assertEquals("UZB", route.get(1));
        assertEquals("AFG", route.getLast());
    }

    @Test
    void shouldReturnSingleCountryForSameOriginAndDestination() {
        var route = routingService.findRoute("CZE", "CZE");
        assertEquals(List.of("CZE"), route);
    }

    @Test
    void shouldThrowWhenNoLandRoute() {
        var exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("CZE", "JPN"));
        assertTrue(exception.getMessage().contains("No land route found from CZE to JPN"));
    }

    @Test
    void shouldThrowWhenBothIslandCountries() {
        var exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("JPN", "GBR"));
        assertTrue(exception.getMessage().contains("No land route found from JPN to GBR"));
    }

    private static Stream<Arguments> notFoundCountryCodes() {
        return Stream.of(
            Arguments.of("XXX", "CZE", "XXX"),
            Arguments.of("CZE", "YYY", "YYY"),
            Arguments.of("XXX", "YYY", "XXX"),
            Arguments.of("cze", "USA", "cze"),
            Arguments.of("USA", "cze", "cze"),
            Arguments.of("usa", "cze", "usa")
        );
    }

    @ParameterizedTest
    @MethodSource("notFoundCountryCodes")
    void shouldThrowWhenCountryCodeNotFound(final String origin, final String destination, String expectedNotFoundCode) {
        var exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute(origin, destination));
        assertTrue(exception.getMessage().contains("Country code not found: " + expectedNotFoundCode));
    }

    private static Stream<Arguments> invalidCountryCodes() {
        return Stream.of(
            Arguments.of(null, "CZE"),
            Arguments.of("CZE", null),
            Arguments.of(null, null),
            Arguments.of("   ", "CZE"),
            Arguments.of("CZE", "   "),
            Arguments.of("", "CZE"),
            Arguments.of("CZE", "")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCountryCodes")
    void shouldThrowWhenCountryCodeIsInvalid(final String origin, final String destination) {
        var exception = assertThrows(IllegalArgumentException.class, () -> routingService.findRoute(origin, destination));
        assertTrue(exception.getMessage().contains("Country code must not be null or blank"));
    }

}
