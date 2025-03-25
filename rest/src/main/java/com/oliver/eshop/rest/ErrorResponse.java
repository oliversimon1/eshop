package com.oliver.eshop.rest;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) { }
