package com.codekopf.router.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

import com.codekopf.router.dto.RouteResponse;
import com.codekopf.router.service.RoutingService;

/**
 * REST controller exposing the land route calculation endpoint.
 */
@RestController
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    /**
     * Calculates a land route between two countries.
     *
     * @param origin      cca3 code of the origin country
     * @param destination cca3 code of the destination country
     * @return the calculated route wrapped in a {@link RouteResponse}
     */
    @GetMapping("/routing/{origin}/{destination}")
    public ResponseEntity<RouteResponse> getRoute(@PathVariable final String origin, @PathVariable final String destination) {
        val route = routingService.findRoute(origin, destination);
        return ResponseEntity.ok(new RouteResponse(route));
    }

}
