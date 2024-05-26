package com.mitocode.controller;

import com.mitocode.model.Dish;
import com.mitocode.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor // Hace la inyeccion por constructor, pero solo toma en cuenta los que tienen "final"
public class DishController {

    private final IDishService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Dish>>> findAll (){
        Flux<Dish>  fx = service.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Dish>> finById (@PathVariable("id") String id){

        return service.findById(id)
                //Tranformar la salida anterior a un ResponseEntity
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        //Contenido en el body.
                        //El body es lo que esta dentro de los parentesis angulares de ResponseEntity
                        //Por ello, el metodo body solo te pide como parametro un objeto de Dish
                        .body(e)
                )
                //Si por alguna razon no se encuentra informacion devuelvo notFound()
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/v2/{id}")
    public Mono<ResponseEntity<Mono<Dish>>> finByIdV2 (@PathVariable("id") String id){
        return service.findById(id)
                //Tranformar la salida anterior a un ResponseEntity
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        //Contenido en el body
                        //El body es lo que esta dentro de los parentesis angulares de ResponseEntity
                        //Por ello, el metodo body te pide como parametro un flujo Mono
                        .body(Mono.just(e))
                )
                //Si por alguna razon no se encuentra informacion devuelvo notFound()
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Mono<ResponseEntity<Dish>> save (@RequestBody Dish dish, final ServerHttpRequest req){
        return service.save(dish)
                .map(e -> ResponseEntity.created(
                                      URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                              )
                              .contentType(MediaType.APPLICATION_JSON)
                              .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Dish>> update (@PathVariable("id") String id, @RequestBody Dish dish){
        return Mono.just(dish)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(id, e))
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                );

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Dish>> delete(@PathVariable("id") String id){
        return service.delete(id)
                .map(e -> ResponseEntity.noContent().build());
    }
}
