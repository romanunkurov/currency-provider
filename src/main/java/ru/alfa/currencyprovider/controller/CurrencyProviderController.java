package ru.alfa.currencyprovider.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alfa.currencyprovider.exception.BadRequestException;
import ru.alfa.currencyprovider.service.CurrencyObserverService;

@RestController
@RequestMapping("/v1/internal/insurance")
@AllArgsConstructor
public class CurrencyProviderController {

    private final CurrencyObserverService service;

    @GetMapping("/toUSD/{ticker}")
    public ResponseEntity<byte[]> compareToUSD(@PathVariable String ticker) throws BadRequestException {

        return ResponseEntity.ok().body(service.getCurrencyCourse(ticker));
    }
}
