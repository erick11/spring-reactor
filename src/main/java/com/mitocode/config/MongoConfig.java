package com.mitocode.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
@RequiredArgsConstructor
public class MongoConfig implements InitializingBean {

    //@Lazy: Carga tardia de los beans
    @Lazy
    private final MappingMongoConverter converter;

    @Override
    public void afterPropertiesSet() throws Exception {
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }



}