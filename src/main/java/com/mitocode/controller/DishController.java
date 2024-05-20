package com.mitocode.controller;

import com.mitocode.model.Dish;
import com.mitocode.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor // Hace la inyeccion por constructor, pero solo toma en cuenta los que tienen "final"
public class DishController {

    private final IDishService service;

    @GetMapping
    public Flux<Dish> findAll (){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Dish> finById (@PathVariable("id") String id){
        return service.findById(id);
    }

    @PostMapping
    public Mono<Dish> save (@RequestBody Dish dish){
        return service.save(dish);
    }

    @PutMapping
    public Mono<Dish> uppdate (@PathVariable("id") String id, @RequestBody Dish dish){
        return Mono.just(dish)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(id, e));

    }

    @DeleteMapping("/{id}")
    public Mono<Boolean> delete(@PathVariable("id") String id){
        return service.delete(id);
    }
}
