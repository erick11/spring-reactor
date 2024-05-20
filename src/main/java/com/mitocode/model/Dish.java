package com.mitocode.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data // Sobre escribe el @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo compara por le ID
@Document(collection = "dishes")
public class Dish {

    @Id
    @EqualsAndHashCode.Include // Solo compara por le ID
    private String id;

    @Field
    private String name;

    @Field
    private Double price;

    @Field
    private Boolean status;

}
