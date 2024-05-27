package com.mitocode.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
//@Order(Ordered.HIGHEST_PRECEDENCE) //Para que tome esta clase en un orden superior
@Order(-1) //Para que tome esta clase en un orden superior
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {


    public WebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorView);
    }

    private Mono<ServerResponse> renderErrorView(ServerRequest req) {

        /*
        captura los atributos del mensaje de error

        "timestamp": "2024-05-27T02:33:24.498+00:00",
        "path": "/dishes",
        "status": 400,
        "error": "Bad Request",
        "requestId": "d1cd712f-3"
        */
        Map<String, Object> generalError = getErrorAttributes(req, ErrorAttributeOptions.defaults());

        //Personalizar el mensaje
        Map<String, Object> customError = new HashMap<>();

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        int statusCode = Integer.parseInt(String.valueOf(generalError.get("status")));
        Throwable error = getError(req);

        switch (statusCode){

            case 400, 422 ->{
                customError.put("messaje", error.getMessage());
                customError.put("status", 400);
                httpStatus = HttpStatus.BAD_REQUEST;
            }
            case 404 ->{
                customError.put("messaje", error.getMessage());
                customError.put("status", 404);
                httpStatus = HttpStatus.NOT_FOUND;
            }
            case 401, 403 ->{
                customError.put("messaje", error.getMessage());
                customError.put("status", 401);
                httpStatus = HttpStatus.UNAUTHORIZED;
            }
            case 500 ->{
                customError.put("messaje", error.getMessage());
                customError.put("status", 500);
                //httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            default -> {
                customError.put("messaje", error.getMessage());
                customError.put("status", 418);
                httpStatus = HttpStatus.I_AM_A_TEAPOT;
            }
        }

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customError));
    }
}
