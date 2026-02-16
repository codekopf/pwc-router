package com.codekopf.router.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.codekopf.router.exception.GlobalExceptionHandler;
import com.codekopf.router.model.AdjacencyGraph;
import com.codekopf.router.service.RoutingService;
import com.codekopf.router.utils.AdjacencyGraphBuilder;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoutingController.class)
@Import({RoutingControllerTest.TestConfig.class, GlobalExceptionHandler.class})
class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Configuration
    @Import({RoutingController.class})
    static class TestConfig {

        @Bean
        public AdjacencyGraph adjacencyGraph() {
            return AdjacencyGraphBuilder.getSimpleAdjacencyGraphForTesting();
        }

        @Bean
        public RoutingService routingService(final AdjacencyGraph adjacencyGraph) {
            return new RoutingService(adjacencyGraph);
        }

    }

    // Happy case

    @Test
    void shouldReturnRouteForValidRequest() throws Exception {
        mockMvc.perform(get("/routing/CZE/ITA"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.route").isArray())
            .andExpect(jsonPath("$.route[0]").value("CZE"))
            .andExpect(jsonPath("$.route[1]").value("AUT"))
            .andExpect(jsonPath("$.route[-1:]").value("ITA"));
    }

    @Test
    void shouldReturnShortestRouteForValidRequest() throws Exception {
        mockMvc.perform(get("/routing/KAZ/AFG"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.route").isArray())
            .andExpect(jsonPath("$.route[0]").value("KAZ"))
            .andExpect(jsonPath("$.route[1]").value("UZB"))
            .andExpect(jsonPath("$.route[-1:]").value("AFG"));
    }

    // Non-existing route

    @Test
    void shouldReturnBadRequestWhenNoLandRoute() throws Exception {
        mockMvc.perform(get("/routing/CZE/JPN"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").exists());
    }

    // Incorrect input - Invalid country codes

    @Test
    void shouldReturnBadRequestForInvalidCountryCodeForOrigin() throws Exception {
        mockMvc.perform(get("/routing/XXX/ITA"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Country code not found: XXX"));
    }

    @Test
    void shouldReturnBadRequestForInvalidCountryCodeForDestination() throws Exception {
        mockMvc.perform(get("/routing/ITA/XXX"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Country code not found: XXX"));
    }

    @Test
    void shouldReturnBadRequestForInvalidCountryCodeForBothOriginAndDestinationReturnFirstInvalid() throws Exception {
        mockMvc.perform(get("/routing/XXX/ZZZ"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Country code not found: XXX"));
    }

    // Incorrect input - Empty and blank input

    @Test
    void shouldReturnBadRequestWhenOriginIsBlank() throws Exception {
        mockMvc.perform(get("/routing/ /CZE"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Country code must not be null or blank"));
    }

    @Test
    void shouldReturnBadRequestWhenDestinationIsBlank() throws Exception {
        mockMvc.perform(get("/routing/CZE/ "))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Country code must not be null or blank"));
    }

     // Edge case

    @Test
    void shouldReturnSingleCountryWhenSameOriginAndDestination() throws Exception {
        mockMvc.perform(get("/routing/CZE/CZE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.route").isArray())
            .andExpect(jsonPath("$.route.length()").value(1))
            .andExpect(jsonPath("$.route[0]").value("CZE"));
    }

}
