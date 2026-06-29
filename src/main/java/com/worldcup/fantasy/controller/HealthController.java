package com.worldcup.fantasy.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lightweight keep-alive / health endpoint.
 * Pinged periodically to keep the Render free-tier instance warm.
 *
 * GET /health  ->  "scoring engine is up :-)"
 */
@RestController
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping(value = "/health", produces = "text/plain")
    public String health() {
        return "scoring engine is up :-)";
    }
}