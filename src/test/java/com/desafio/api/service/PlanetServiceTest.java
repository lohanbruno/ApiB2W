package com.desafio.api.service;

import com.desafio.api.ApiApplication;
import com.desafio.api.model.Planet;
import com.desafio.api.repository.PlanetRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApiApplication.class)
public class PlanetServiceTest {

    @SpyBean
    private PlanetService planetService;

    @MockBean
    private PlanetRepository planetRepository;

    @MockBean
    private ApiService apiService;

    private Planet planetOne;
    private Planet planetTwo;
    private List<Planet> planets;

    private static final String BODY_RESPONSE = "{\n" +
            "  \"count\": 1,\n" +
            "  \"next\": null,\n" +
            "  \"previous\": null,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"name\": \"Hoth\",\n" +
            "      \"rotation_period\": \"23\",\n" +
            "      \"orbital_period\": \"549\",\n" +
            "      \"diameter\": \"7200\",\n" +
            "      \"climate\": \"frozen\",\n" +
            "      \"gravity\": \"1.1 standard\",\n" +
            "      \"terrain\": \"tundra, ice caves, mountain ranges\",\n" +
            "      \"surface_water\": \"100\",\n" +
            "      \"population\": \"unknown\",\n" +
            "      \"residents\": [],\n" +
            "      \"films\": [\n" +
            "        \"https://swapi.co/api/films/2/\"\n" +
            "      ],\n" +
            "      \"created\": \"2014-12-10T11:39:13.934000Z\",\n" +
            "      \"edited\": \"2014-12-20T20:58:18.423000Z\",\n" +
            "      \"url\": \"https://swapi.co/api/planets/4/\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Before
    public void init() {
        planetOne = new Planet();
        planetOne.setId("1L");
        planetOne.setName("Mars");
        planetOne.setAppearances(5);

        planetTwo = new Planet();
        planetTwo.setId("2L");
        planetTwo.setName("Earth");

        planets = Arrays.asList(planetOne, planetTwo);
    }

    @Test
    public void findByName_should_return_planet_when_it_exists() {
        when(planetRepository.findByName("Mars")).thenReturn(Optional.ofNullable(planetOne));
        Optional<Planet> planetFound = planetService.findByName("Mars");

        assertTrue(planetFound.isPresent());
        assertEquals("Mars", planetFound.get().getName());
        assertEquals("1L", planetFound.get().getId());
        assertEquals(5, planetFound.get().getAppearances());
    }

    @Test
    public void findByName_should_return_planet_when_it_exists_adjusting_name_to_match() {
        when(planetRepository.findByName("Mars")).thenReturn(Optional.ofNullable(planetOne));
        Optional<Planet> planetFound = planetService.findByName("MARS");

        assertTrue(planetFound.isPresent());
        assertEquals("Mars", planetFound.get().getName());
        assertEquals("1L", planetFound.get().getId());
        assertEquals(5, planetFound.get().getAppearances());
    }

    @Test
    public void findByName_should_return_empty_optional_when_it_doesnt_find_a_planet_with_that_name() {
        Optional<Planet> planetFound = planetService.findByName("JohnDoe");

        assertFalse(planetFound.isPresent());
    }

    @Test
    public void findById_should_return_planet_when_it_exists(){
        when(planetRepository.findById("1L")).thenReturn(Optional.ofNullable(planetOne));
        Optional<Planet> planetFound = planetService.findById("1L");

        assertTrue(planetFound.isPresent());
        assertEquals("Mars", planetFound.get().getName());
        assertEquals("1L", planetFound.get().getId());
        assertEquals(5, planetFound.get().getAppearances());
    }

    @Test
    public void findById_should_return_empty_optional_when_it_doesnt_find_a_planet_with_that_id() {
        Optional<Planet> planetFound = planetService.findById("-99");

        assertFalse(planetFound.isPresent());
    }

    @Test
    public void findAll_should_return_all_planets(){
        when(planetRepository.findAll()).thenReturn(planets);
        List<Planet> planetsFound = planetService.findAll();

        assertEquals(2, planetsFound.size());

        planetsFound.sort(Comparator.comparing(Planet::getName));

        assertEquals("Earth", planetsFound.get(0).getName());
        assertEquals("Mars", planetsFound.get(1).getName());
    }

    @Test
    public void delete_should_delete_planet(){
        planetService.delete(planetOne);

        verify(planetRepository, times(1)).delete(planetOne);
        verifyNoMoreInteractions(planetRepository);
    }

    @Test
    public void savePlanet_should_count_appearances_and_save_planet(){
        doReturn(15).when(planetService).countPlanetAppearances(anyString());
        when(planetRepository.save(any(Planet.class))).thenReturn(planetTwo);

        assertEquals("Earth", planetTwo.getName());
        assertEquals("2L", planetTwo.getId());
        assertEquals(0, planetTwo.getAppearances());
        Planet planetSaved = planetService.savePlanet(planetTwo);

        assertEquals("Earth", planetSaved.getName());
        assertEquals("2L", planetSaved.getId());
        assertEquals(15, planetSaved.getAppearances());
    }

    @Test
    public void countPlanetAppearances_should_return_appearances_from_json_response() throws IOException {
        ResponseEntity<String> response = new ResponseEntity<>(BODY_RESPONSE, HttpStatus.OK);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());

        when(apiService.searchForSwapiEntity(anyString(), anyString())).thenReturn(root);
        assertEquals(1, planetService.countPlanetAppearances("Hoth"));
    }












}
