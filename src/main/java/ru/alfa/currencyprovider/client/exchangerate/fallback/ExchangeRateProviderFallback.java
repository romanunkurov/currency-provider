package ru.alfa.currencyprovider.client.exchangerate.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.alfa.currencyprovider.client.exchangerate.ExchangeRateProvider;
import ru.alfa.currencyprovider.dto.CurrencyDTO;
import java.util.Date;

import static ru.alfa.currencyprovider.client.ClientUtil.getFallbackResponseEntity;

@Slf4j
public class ExchangeRateProviderFallback implements ExchangeRateProvider {

    private final Throwable exception;

    public ExchangeRateProviderFallback(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public ResponseEntity<CurrencyDTO> getLatestRate(String appId, String base) {
        log.error("Ошибка при обращении к  https://docs.openexchangerates.org/", exception);
        return getFallbackResponseEntity(exception, new CurrencyDTO());
    }

    @Override
    public ResponseEntity<CurrencyDTO> getHistoricalRate(Date date, String appId, String base) {
        log.error("Ошибка при обращении к  https://docs.openexchangerates.org/", exception);
        return getFallbackResponseEntity(exception, new CurrencyDTO());
    }
}
