package com.codekopf.router.service;

import lombok.val;

import com.codekopf.router.exception.RouteNotFoundException;
import com.codekopf.router.utils.AdjacencyMapBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
