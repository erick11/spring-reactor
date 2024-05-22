package com.mitocode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data // Sobre escribe el @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo compara por le ID
@Document(collection = "menus")
public class Menu {

    @Id
    @EqualsAndHashCode.Include // Solo compara por le ID
    private String id;

    @Field
    private String icon;

    @Field
    private String name;

    @Field
    private String url;

    @Field
    private List<String>  roles;
}
