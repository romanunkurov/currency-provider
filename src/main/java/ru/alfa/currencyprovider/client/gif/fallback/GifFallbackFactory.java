package ru.alfa.currencyprovider.client.gif.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.alfa.currencyprovider.client.gif.GifProvider;

@Component
public class GifFallbackFactory implements FallbackFactory<GifProvider> {

    @Override
    public GifProvider create(Throwable throwable) {
        return new GifProviderFallback(throwable);
    }
}
