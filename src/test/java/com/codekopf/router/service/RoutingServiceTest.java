package com.codekopf.router.service;

import java.util.List;

import lombok.val;

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
        val adjacencyMap = AdjacencyMapBuilder.getSimpleAdjacencyMapForTesting();
        routingService = new RoutingService(adjacencyMap);
    }

    @Test
    void shouldFindDirectRoute() {
        val route = routingService.findRoute("CZE", "AUT");
        assertEquals(List.of("CZE", "AUT"), route);
    }

    @Test
    void shouldFindRouteViaIntermediateCountry() {
        val route = routingService.findRoute("CZE", "ITA");
        assertEquals("CZE", route.getFirst());
        assertEquals("ITA", route.getLast());
        assertTrue(route.size() <= 3, "Route should be at most 3 hops: CZE->AUT->ITA");
    }

    @Test
    void shouldFindLongerRoute() {
        val route = routingService.findRoute("CZE", "ESP");
        assertEquals("CZE", route.getFirst());
        assertEquals("ESP", route.getLast());
        assertTrue(route.size() >= 3, "Route from CZE to ESP should pass through at least one intermediate country");
    }

    @Test
    void shouldReturnSingleCountryForSameOriginAndDestination() {
        val route = routingService.findRoute("CZE", "CZE");
        assertEquals(List.of("CZE"), route);
    }

    @Test
    void shouldThrowWhenNoLandRoute() {
        val exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("CZE", "JPN"));
        assertTrue(exception.getMessage().contains("No land route found"));
    }

    @Test
    void shouldThrowWhenBothIslandCountries() {
        val exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("JPN", "GBR"));
        assertTrue(exception.getMessage().contains("No land route found"));
    }

    @Test
    void shouldThrowWhenOriginNotFound() {
        val exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("XXX", "CZE"));
        assertTrue(exception.getMessage().contains("Country code not found"));
    }

    @Test
    void shouldThrowWhenDestinationNotFound() {
        val exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("CZE", "YYY"));
        assertTrue(exception.getMessage().contains("Country code not found"));
    }

    @Test
    void shouldThrowWhenOriginAndDestinationNotFound() {
        val exception = assertThrows(RouteNotFoundException.class, () -> routingService.findRoute("XXX", "YYY"));
        assertTrue(exception.getMessage().contains("Country code not found"));
    }

}
