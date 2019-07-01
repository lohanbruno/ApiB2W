package com.desafio.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter @Setter
@ToString
@Document(collection = "planets")
public class Planet {
    @Id
    private String id;
    @NotNull @Indexed(unique = true)
    private String name;
    @NotNull
    private String climate;
    @NotNull
    private String terrain;

    private int appearances;


}
