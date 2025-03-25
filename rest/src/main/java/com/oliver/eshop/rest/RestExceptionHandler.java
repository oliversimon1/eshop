package com.oliver.eshop.rest;

import com.oliver.eshop.domain.exception.ValidationException;
import com.oliver.eshop.service.order.exception.OrderNotFoundException;
import com.oliver.eshop.service.product.exception.ProductInActiveOrderException;
import com.oliver.eshop.service.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({OrderNotFoundException.class, ProductNotFoundException.class})
    public final ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler({ValidationException.class, ProductInActiveOrderException.class})
    public final ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                ));
    }
}
