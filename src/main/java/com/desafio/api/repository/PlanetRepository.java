package com.desafio.api.repository;

import com.desafio.api.model.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlanetRepository  extends MongoRepository <Planet, String> {

    Optional<Planet> findByName(String name);
}
