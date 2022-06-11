package ru.alfa.currencyprovider.exception;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

public class BadRequestException extends HttpResponseException {

    private static final long serialVersionUID = 2959363356183042450L;

    public BadRequestException(int statusCode, String reasonPhrase) {
        super(statusCode, reasonPhrase);
    }

    public BadRequestException(String reasonPhrase) {
        super(HttpStatus.SC_BAD_REQUEST, reasonPhrase);
    }
}

