package ru.alfa.currencyprovider.client.exchangerate.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.alfa.currencyprovider.client.exchangerate.ExchangeRateProvider;

@Component
public class ExchangeRateFallbackFactory implements FallbackFactory<ExchangeRateProvider> {

    @Override
    public ExchangeRateProvider create(Throwable throwable) {
        return new ExchangeRateProviderFallback(throwable);
    }
}
