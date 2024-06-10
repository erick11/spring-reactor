package com.mitocode.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSupport<T> {

    public static final String FIRST_PAGE_NUM = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";

    private List<T> content; // El contenido que el cliente quiere ver. El contendo parcializado los 5 primeros, los 10 primeros, los 20 primeros
    private int pageNumber; // La pagina donde te encuentras
    private int pageSize; // La cantidad de elementos por pagina
    private int totalElements; //El total de elementos en la base de datos

    @JsonProperty // la salida de este metodo sea un Json
    public long totalPages() {
        return pageSize > 0 ? (totalElements -  1)/ pageSize+ 1 : 0;
    }

    @JsonProperty
    public boolean  first() {
        return pageNumber == Integer.parseInt(FIRST_PAGE_NUM);
    }

    @JsonProperty
    public boolean  last() {
        return (pageNumber +1)* pageSize >= totalElements;
    }

}
