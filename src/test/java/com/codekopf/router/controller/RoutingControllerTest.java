package com.codekopf.router.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.codekopf.router.service.RoutingService;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoutingController.class)
@Import(RoutingControllerTest.TestConfig.class)
class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Configuration
    @Import({RoutingController.class})
    static class TestConfig {

        @Bean
        public RoutingService routingService() {
            return new RoutingService();
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

    // Non-existing route

    @Test
    void shouldReturnBadRequestWhenNoLandRoute() throws Exception {
        mockMvc.perform(get("/routing/CZE/JPN"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").exists());
    }

    // Incorrect input

    @Test
    void shouldReturnBadRequestForInvalidCountryCodeForSource() throws Exception {
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
    void shouldReturnBadRequestForInvalidCountryCodeForBothSourceAndDestinationReturnFirstInvalid() throws Exception {
        mockMvc.perform(get("/routing/XXX/ZZZ"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Country code not found: XXX"));
    }

     // Edge cases

    @Test
    void shouldReturnSingleCountryWhenSameOriginAndDestination() throws Exception {
        mockMvc.perform(get("/routing/CZE/CZE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.route").isArray())
            .andExpect(jsonPath("$.route.length()").value(1))
            .andExpect(jsonPath("$.route[0]").value("CZE"));
    }

}
