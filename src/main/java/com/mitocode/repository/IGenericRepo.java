package com.mitocode.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericRepo<T, ID>  extends ReactiveMongoRepository<T, ID> {

    /*
       Couldn't find PersistentEntity for type class java.lang.Object
       Basicamente te esta diciendo que Objet no tiene una unidad de Persistencia
       Es decir un @Document. Por ello, usamos @NoRepositoryBean
    */

}
