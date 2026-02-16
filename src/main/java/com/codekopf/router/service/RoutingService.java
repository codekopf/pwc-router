package com.codekopf.router.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutingService {

    /**
     * Finds a land route between two countries identified by their cca3 codes.
     *
     * @param origin      the cca3 code of the origin country
     * @param destination the cca3 code of the destination country
     * @return ordered a list of cca3 country codes representing the route
     */
    public List<String> findRoute(final String origin, final String destination) {
        return List.of(origin, destination);
    }

}
