package ru.alfa.currencyprovider.client.gif.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.alfa.currencyprovider.client.gif.GifProvider;
import ru.alfa.currencyprovider.dto.GifDTO;

import static ru.alfa.currencyprovider.client.ClientUtil.getFallbackResponseEntity;

@Slf4j
public class GifProviderFallback implements GifProvider {

    private final Throwable exception;

    public GifProviderFallback(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public ResponseEntity<GifDTO> getGif(String apiKey, String query, Integer limit, Integer offset, String rating, String lan) {
        log.error("Ошибка при обращении к https://api.giphy.com", exception);
        return getFallbackResponseEntity(exception, new GifDTO());
    }
}
