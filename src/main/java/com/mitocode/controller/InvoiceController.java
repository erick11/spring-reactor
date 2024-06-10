package com.mitocode.controller;

import com.mitocode.dto.InvoiceDTO;
import com.mitocode.model.Invoice;
import com.mitocode.pagination.PageSupport;
import com.mitocode.service.IInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;


@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor // Hace la inyeccion por constructor, pero solo toma en cuenta los que tienen "final"
public class InvoiceController {

    private final IInvoiceService service;

    @Qualifier("invoiceMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<InvoiceDTO>>> findAll() {
        // .map: pide como parametro una interface funcional Function
        // Function: T parametro de entrada y R es el resultado => e -> modelMapper.map(e, InvoiceDTO.class)
        Flux<InvoiceDTO> fx = service.findAll().map(this::converToDTO);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<InvoiceDTO>> finById(@PathVariable("id") String id) {

        return service.findById(id)
                .map(this::converToDTO)
                //Tranformar la salida anterior a un ResponseEntity
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        //Contenido en el body.
                        //El body es lo que esta dentro de los parentesis angulares de ResponseEntity
                        //Por ello, el metodo body solo te pide como parametro un objeto de Invoice
                        .body(e)
                )
                //Si por alguna razon no se encuentra informacion devuelvo notFound()
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/v2/{id}")
    public Mono<ResponseEntity<Mono<Invoice>>> finByIdV2(@PathVariable("id") String id) {
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
    public Mono<ResponseEntity<InvoiceDTO>> save(@Valid @RequestBody InvoiceDTO dto, final ServerHttpRequest req) {
        return service.save(converToDocument(dto))
                .map(this::converToDTO)
                .map(e -> ResponseEntity.created(
                                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<InvoiceDTO>> update(@Valid @PathVariable("id") String id, @RequestBody InvoiceDTO dto) {
        return Mono.just(dto)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(id, converToDocument(e)))
                .map(this::converToDTO)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return service.delete(id)
                .flatMap(result -> {
                    if (result) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    private InvoiceDTO invoiceHateoas;
    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<InvoiceDTO>> getHateoas (@PathVariable ("id") String id) {
        Mono <Link> monoLink =  linkTo(methodOn(InvoiceController.class).finById(id)).withRel("invoice-info").toMono();

        //PRACTICA ES COMUN PERO NO RECOMENADA
        /*
        return service.findById(id)
                .map(this::converToDTO)
                .flatMap(d -> {
                    this.invoiceHateoas = d;
                    return monoLink;
                })
                .map(link -> EntityModel.of(this.invoiceHateoas,link));
         */

        //PRACTICA INTERMEDIA
        /*
        return service.findById(id)
                .map(this::converToDTO)
                .flatMap(d -> monoLink.map(link -> EntityModel.of(d, link)));
         */
        //PRACTICA IDEAL
        return service.findById(id)
                .map(this::converToDTO)
                .zipWith(monoLink, EntityModel::of); //.zipWith(monoLink, (d, link) -> EntityModel.of(d, link));

        //return EntityModel.of(objeto, link);
    }

    @GetMapping("/pageable")
    private Mono<ResponseEntity<PageSupport<InvoiceDTO>>> getPage(
            @RequestParam (name =  "page", defaultValue = "0") int page,
            @RequestParam (name =  "size", defaultValue = "2") int size
    ){
        return service.getPage(PageRequest.of(page,size))
                .map(pageSupport -> new PageSupport<>(
                        pageSupport.getContent().stream().map(this::converToDTO).toList(), //map(c -> converToDTO(c))
                        pageSupport.getPageNumber(),
                        pageSupport.getPageSize(),
                        pageSupport.getTotalElements()
                ))
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private InvoiceDTO converToDTO (Invoice model){
        return  modelMapper.map(model, InvoiceDTO.class);
    }

    private Invoice converToDocument (InvoiceDTO dto){
        return modelMapper.map(dto, Invoice.class);
    }
}
