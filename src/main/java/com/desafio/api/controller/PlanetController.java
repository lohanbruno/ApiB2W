package com.desafio.api.controller;

import com.desafio.api.model.Planet;
import com.desafio.api.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Planet>> findPlanets(){
        return ResponseEntity.ok(planetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> findPlanetById(@PathVariable("id") String id) {
        Optional<Planet> planetFound = planetService.findById(id);
        return planetFound.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @RequestMapping(value = "", params = "name", method = RequestMethod.GET)
    public ResponseEntity<Planet> findPlanetByName(@RequestParam String name) {
        Optional<Planet> planetFound = planetService.findByName(name);
        return planetFound.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlanet(@PathVariable("id") String id) {
        Optional<Planet> planetFound = planetService.findById(id);
        return planetFound.map(planet -> {
            planetService.delete(planet);
            return ResponseEntity.noContent().build();
        })
        .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("")
    public ResponseEntity<Object> createPlanet(@Valid @RequestBody Planet planet) {
        if (planetService.findByName(planet.getName()).isPresent()){return ResponseEntity.status(HttpStatus.CONFLICT).build();}

        Planet planetCreated = planetService.savePlanet(planet);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(planetCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(planetCreated);
    }
}
