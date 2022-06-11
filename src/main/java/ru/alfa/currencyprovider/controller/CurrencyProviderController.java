package ru.alfa.currencyprovider.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alfa.currencyprovider.exception.BadRequestException;
import ru.alfa.currencyprovider.service.CurrencyObserverService;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/v1/currency/provider")
@AllArgsConstructor
public class CurrencyProviderController {

    private final CurrencyObserverService service;

    @GetMapping(value = "/toUSD/{ticker}")
    public void compareToUSD(@PathVariable String ticker, HttpServletResponse response) throws BadRequestException, IOException {
        response.setContentType(MediaType.IMAGE_GIF_VALUE);
        IOUtils.copy(new ByteArrayInputStream(service.getCurrencyCourse(ticker)), response.getOutputStream());
    }
}
