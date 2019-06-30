package com.desafio.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping(value = "/health", produces = "application/json; charset=utf-8")
    public String getHealthCheck()
    {
        return "{ 'status' : UP }";
    }
}
