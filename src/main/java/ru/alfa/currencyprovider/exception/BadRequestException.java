package ru.alfa.currencyprovider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class BadRequestException extends HttpServerErrorException {

    private static final long serialVersionUID = 2959363356183042450L;

    public BadRequestException(String reasonPhrase) {
        super(HttpStatus.BAD_REQUEST, reasonPhrase);
    }
}

