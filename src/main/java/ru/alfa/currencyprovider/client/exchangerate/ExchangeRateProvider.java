package ru.alfa.currencyprovider.client.exchangerate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfa.currencyprovider.client.exchangerate.configuration.ExchangeRateProviderConfig;
import ru.alfa.currencyprovider.client.exchangerate.fallback.ExchangeRateFallbackFactory;
import ru.alfa.currencyprovider.dto.CurrencyDTO;

import java.util.Date;

@FeignClient(
        name = "exchangeCurrencyProvider",
        url = "${ru.alfa.currencyprovider.exchange.url}",
        fallbackFactory = ExchangeRateFallbackFactory.class,
        configuration = ExchangeRateProviderConfig.class)
public interface ExchangeRateProvider {

    @GetMapping("/api/latest.json")
    ResponseEntity<CurrencyDTO> getLatestRate(@RequestParam(name = "app_id") String appId,
                                             @RequestParam(required = false) String base);

    @GetMapping("/api/historical/{date}.json")
    ResponseEntity<CurrencyDTO> getHistoricalRate(@PathVariable Date date,
                                  @RequestParam(name = "app_id") String appId,
                                  @RequestParam(required = false) String base);
}