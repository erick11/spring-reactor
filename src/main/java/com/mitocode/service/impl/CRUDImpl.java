package com.mitocode.service.impl;

import com.mitocode.pagination.PageSupport;
import com.mitocode.repository.IGenericRepo;
import com.mitocode.service.ICRUD;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CRUDImpl <T, ID> implements ICRUD<T, ID> {

    /*
    Para que una clase implemete metodos abstractos la clase debe ser abstracta
    * */
    protected abstract IGenericRepo<T, ID> getRepo();

    @Override
    public Mono<T> save(T t) {
        return getRepo().save(t);
    }

    @Override
    public Mono<T> update(ID id, T t) {
        //Nota: Ver que el metodo save guarda T. Que es el objeto se envia como parametro
        return getRepo().findById(id).flatMap(e -> getRepo().save(t)) ;
    }

    @Override
    public Flux<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepo().findById(id);
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        return getRepo().findById(id)
                .hasElement()// despues de la evaluacion de getRepo().findById(id) devuelve true o false
                .flatMap(result -> {
                    if (result){
                        return getRepo().deleteById(id).thenReturn(true);
                        //return getRepo().deleteById(id).then(Mono.just(true));
                    }else {
                        return Mono.just(false);
                    }
                });
    }

    @Override
    public Mono<PageSupport<T>> getPage(Pageable pageable) {
        return getRepo().findAll()
                .collectList()
                .map(list -> new PageSupport<>(
                        //1,2,3,4,5,6,7,8,9,10
                        list.stream()
                                .skip(pageable.getPageNumber() * pageable.getPageSize())
                                .limit(pageable.getPageSize()).toList(),
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        list.size()
                ));
    }
}
