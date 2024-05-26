package com.mitocode.service.impl;

import com.mitocode.model.Client;
import com.mitocode.repository.IClientRepo;
import com.mitocode.repository.IGenericRepo;
import com.mitocode.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // Hace la inyeccion por constructor, pero solo toma en cuenta los que tienen "final"
public class ClientServiceImpl extends CRUDImpl<Client, String> implements IClientService {

    private final IClientRepo repo;


    @Override
    protected IGenericRepo<Client, String> getRepo() {
        return repo;
    }
}
