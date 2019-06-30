package com.desafio.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter @Setter
@ToString
@Document(collection = "planets")
public class Planet {
    @Id
    @NonNull
    private String id;
    @NonNull @Indexed(unique = true)
    private String name;
    @NonNull
    private String climate;
    @NonNull
    private String terrain;

    private int appearances;


}
