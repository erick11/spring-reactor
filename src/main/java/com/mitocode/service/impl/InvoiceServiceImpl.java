package com.mitocode.service.impl;

import com.mitocode.model.Invoice;
import com.mitocode.repository.IInvoiceRepo;
import com.mitocode.repository.IGenericRepo;
import com.mitocode.service.IInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // Hace la inyeccion por constructor, pero solo toma en cuenta los que tienen "final"
public class InvoiceServiceImpl extends CRUDImpl<Invoice, String>  implements IInvoiceService {

    private final IInvoiceRepo repo;

    @Override
    protected IGenericRepo<Invoice, String> getRepo() {
        return repo;
    }


    /*
    @Override
    public Mono<Invoice> save(Invoice invoice) {
        return repo.save(invoice);
    }

    @Override
    public Mono<Invoice> update(String id, Invoice invoice) {
        //VERIFICACION CON EL ID
        return repo.save(invoice);
    }

    @Override
    public Flux<Invoice> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Invoice> findById(String id) {
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
