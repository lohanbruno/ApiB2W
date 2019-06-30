package com.desafio.api.controller;

import com.desafio.api.model.Planet;
import com.desafio.api.service.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetService planetService;

    private Planet planetOne;
    private Planet planetTwo;
    private List<Planet> planets;

    @Before
    public void init(){
        planetOne = new Planet();
        planetOne.setId("1L");
        planetOne.setName("Earth");
        planetOne.setAppearances(10);
        planetOne.setClimate("Warm");
        planetOne.setTerrain("Rocky");

        planetTwo = new Planet();
        planetTwo.setId("2L");
        planetTwo.setName("Mars");
        planetTwo.setAppearances(5);

        planets = Arrays.asList(planetOne,planetTwo);
    }

    @Test
    public void findPlanets_should_return_ok_and_every_planet() throws Exception{
        when(planetService.findAll()).thenReturn(planets);

        this.mockMvc.perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':'1L','name':'Earth','climate':Warm,'terrain':Rocky,'appearances':10}" +
                        ",{'id':'2L','name':'Mars','climate':null,'terrain':null,'appearances':5}]"));
    }

    @Test
    public void findPlanetById_should_return_ok_and_planet_when_it_exists() throws Exception {
        when(planetService.findById("1L")).thenReturn(Optional.of(planetOne));

        this.mockMvc.perform(get("/planets/1L"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':'1L','name':'Earth','climate':Warm,'terrain':Rocky,'appearances':10}"));
    }

    @Test
    public void findPlanetById_should_return_not_found_when_planet_doesnt_exists() throws Exception {
        this.mockMvc.perform(get("/planets/-99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findPlanetByName_should_return_ok_and_planet_when_it_exists() throws Exception {
        when(planetService.findByName("Earth")).thenReturn(Optional.of(planetOne));

        this.mockMvc.perform(get("/planets?name=Earth"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':'1L','name':'Earth','climate':Warm,'terrain':Rocky,'appearances':10}"));
    }

    @Test
    public void findPlanetByName_should_return_not_found_when_planet_doesnt_exists() throws Exception {
        this.mockMvc.perform(get("/planets?name=fake"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePlanet_should_return_no_content_and_delete_planet_when_it_exists() throws Exception {
        when(planetService.findById("1L")).thenReturn(Optional.of(planetOne));

        this.mockMvc.perform(delete("/planets/1L"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePlanet_should_return_not_found_when_planet_doesnt_exists() throws Exception {
        this.mockMvc.perform(delete("/planets/-99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createPlanet_should_return_ok_and_planet_when_it_exists() throws Exception {
        when(planetService.savePlanet(any(Planet.class))).thenReturn(planetOne);
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/planets")
                .content(objectMapper.writeValueAsString(planetOne))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'id':'1L','name':'Earth','climate':Warm,'terrain':Rocky,'appearances':10}"));
    }

    @Test
    public void createPlanet_should_return_conflict_when_the_planet_already_exists() throws Exception {
        when(planetService.findByName("Earth")).thenReturn(Optional.of(planetOne));
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/planets")
                .content(objectMapper.writeValueAsString(planetOne))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void createPlanet_should_return_bad_request_when_sends_an_invalid_planet() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        //planetTwo has a null terrain and a null climate so its invalid
        this.mockMvc.perform(post("/planets")
                .content(objectMapper.writeValueAsString(planetTwo))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



}
