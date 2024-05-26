package com.mitocode.service.impl;

import com.mitocode.model.Dish;
import com.mitocode.repository.IDishRepo;
import com.mitocode.repository.IGenericRepo;
import com.mitocode.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // Hace la inyeccion por constructor, pero solo toma en cuenta los que tienen "final"
public class DishServiceImpl  extends CRUDImpl<Dish, String>  implements IDishService {

    private final IDishRepo repo;

    @Override
    protected IGenericRepo<Dish, String> getRepo() {
        return repo;
    }


    /*
    @Override
    public Mono<Dish> save(Dish dish) {
        return repo.save(dish);
    }

    @Override
    public Mono<Dish> update(String id, Dish dish) {
        //VERIFICACION CON EL ID
        return repo.save(dish);
    }

    @Override
    public Flux<Dish> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Dish> findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return repo.deleteById(id)
                .then(Mono.just(true));// Despues del proceso anterior continua con un siguiente resultado
                //.thenReturn() //Equivalente a la linea anterior

    }
    */
}
