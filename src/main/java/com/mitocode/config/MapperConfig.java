package com.mitocode.config;

import com.mitocode.dto.ClientDTO;
import com.mitocode.dto.InvoiceDTO;
import com.mitocode.model.Client;
import com.mitocode.model.Invoice;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class MapperConfig {

    @Bean("defaultMapper")
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean("clientMapper")
    public ModelMapper clientMapper(){
        //return new ModelMapper();

        ModelMapper mapper =  new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
         /*LAS NO COINCIDENCIAS*/

        //Escritura
        mapper.createTypeMap(ClientDTO.class, Client.class)
                .addMapping(ClientDTO::getName, (dest, v) -> dest.setFirstName((String) v))//.addMapping(e -> e.getName(), (dest, v) -> dest.setFirstName((String) v))
                .addMapping(ClientDTO::getSurname, (dest, v) -> dest.setLastName((String) v))
                .addMapping(ClientDTO::getBirthDateClient, (dest, v) -> dest.setBirthDate((LocalDate) v))
                .addMapping(ClientDTO::getPicture, (dest, v) -> dest.setUrlPhoto((String) v));

        //Lectura
        mapper.createTypeMap(Client.class, ClientDTO.class)
                .addMapping(Client::getFirstName, (dest, v) -> dest.setName((String) v))//.addMapping(e -> e.getFirstName(), (dest, v) -> dest.setName((String) v))
                .addMapping(Client::getLastName, (dest, v) -> dest.setSurname((String) v))
                .addMapping(Client::getBirthDate, (dest, v) -> dest.setBirthDateClient((LocalDate) v))
                .addMapping(Client::getUrlPhoto, (dest, v) -> dest.setPicture((String) v));

        return mapper;
    }

    @Bean("invoiceMapper")
    public ModelMapper invoiceMapper(){
        //return new ModelMapper();

        ModelMapper mapper =  new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        /*LAS NO COINCIDENCIAS*/

        //Escritura
        mapper.createTypeMap(InvoiceDTO.class, Invoice.class)
                .addMapping( e -> e.getClient().getName(), (dest, v) -> dest.getClient().setFirstName((String)v))
                .addMapping( e -> e.getClient().getSurname(), (dest, v) -> dest.getClient().setLastName((String)v));

        //Lectura
        mapper.createTypeMap(Invoice.class, InvoiceDTO.class)
                .addMapping( e -> e.getClient().getFirstName(), (dest, v) -> dest.getClient().setName((String)v))
                .addMapping( e -> e.getClient().getLastName(), (dest, v) -> dest.getClient().setSurname((String)v));

        return mapper;
    }


}
