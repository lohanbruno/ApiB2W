package com.desafio.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class ApiService {

    private RestTemplate restTemplate;

    @Autowired
    public ApiService(RestTemplateBuilder builder){this.restTemplate = builder.build();}

    public JsonNode searchForSwapiEntity(String entity, String searchQuery) {
        final String searchUrl = (searchQuery == null)
                ? ("https://swapi.co/api/" + entity)
                : ("https://swapi.co/api/" + entity + "/?search=" + searchQuery);

        ResponseEntity<String> response = restTemplate.getForEntity(searchUrl, String.class);
        return buildATreeUsingSwapiReturn(response);
    }

    private JsonNode buildATreeUsingSwapiReturn(ResponseEntity<String> response) {
        JsonNode jsonNode = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (IOException io) {
            io.printStackTrace();
        }
            return jsonNode;
    }



}
