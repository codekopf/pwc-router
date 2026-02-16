package com.codekopf.router.service;

import java.util.List;

import com.codekopf.router.exception.RouteNotFoundException;
import com.codekopf.router.utils.AdjacencyMapBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoutingServiceTest {

    private RoutingService routingService;

    @BeforeEach
    void setUp() {
        var adjacencyMap = AdjacencyMapBuilder.getSimpleAdjacencyMapForTesting();
        routingService = new RoutingService(adjacencyMap);
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

    @Test
    void shouldThrowWhenOriginNotFound() {
        var exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("XXX", "CZE"));
        assertTrue(exception.getMessage().contains("Country code not found: XXX"));
    }

    @Test
    void shouldThrowWhenDestinationNotFound() {
        var exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("CZE", "YYY"));
        assertTrue(exception.getMessage().contains("Country code not found: YYY"));
    }

    @Test
    void shouldThrowWhenOriginAndDestinationNotFound() {
        var exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("XXX", "YYY"));
        assertTrue(exception.getMessage().contains("Country code not found: XXX"));
    }

}
