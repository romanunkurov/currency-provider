package ru.alfa.currencyprovider.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alfa.currencyprovider.exception.BadRequestException;
import ru.alfa.currencyprovider.service.CurrencyObserverService;

@RestController
@RequestMapping("/v1/internal/insurance")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrencyProviderController {

    private final CurrencyObserverService service;


    @GetMapping("/toUSD/{ticker}")
    public ResponseEntity<byte[]> compareToUSD(@PathVariable String ticker) throws BadRequestException {

        return ResponseEntity.ok().body(service.getCurrencyCourse(ticker));
    }
}
