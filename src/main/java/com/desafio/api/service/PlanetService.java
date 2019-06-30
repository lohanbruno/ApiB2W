package com.desafio.api.service;

import com.desafio.api.model.Planet;
import com.desafio.api.repository.PlanetRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private PlanetRepository planetRepository;

    public int countPlanetAppearances(String planetName) {
        JsonNode root = apiService.searchForSwapiEntity("planets", planetName);
        JsonNode results = root.get("results");
        return results.get(0).get("films").size();
    }

    public Planet savePlanet(Planet planet) {
        planet.setAppearances(countPlanetAppearances(planet.getName()));
        return planetRepository.save(planet);
    }

    public Optional<Planet> findByName(String planetName) {
        return planetRepository.findByName(StringUtils.capitalize(planetName.toLowerCase()));
    }

    public Optional<Planet> findById(String id) {
        return planetRepository.findById(id);
    }

    public void delete(Planet planet) {
        planetRepository.delete(planet);
    }

    public List<Planet> findAll() {
        return planetRepository.findAll();
    }

}
