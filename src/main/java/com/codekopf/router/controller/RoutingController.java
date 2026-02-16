package com.codekopf.router.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoutingController {

    @GetMapping("/routing/{origin}/{destination}")
    public ResponseEntity<String> getRoute(@PathVariable final String origin, @PathVariable final String destination) {
        return ResponseEntity.ok("OK");
    }

}
