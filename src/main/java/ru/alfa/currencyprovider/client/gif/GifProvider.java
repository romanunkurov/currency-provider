package ru.alfa.currencyprovider.client.gif;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfa.currencyprovider.client.gif.configuration.GifProviderConfig;
import ru.alfa.currencyprovider.client.gif.fallback.GifFallbackFactory;
import ru.alfa.currencyprovider.dto.GifDTO;

@FeignClient(
        name = "myGifProvider",
        url = "${ru.alfa.currencyprovider.gif.url}",
        fallbackFactory = GifFallbackFactory.class,
        configuration = GifProviderConfig.class)
public interface GifProvider {

    @GetMapping("/v1/gifs/search")
    ResponseEntity<GifDTO> getGif(@RequestParam(name = "api_key") String apiKey,
                                 @RequestParam(name = "q") String query,
                                 @RequestParam(name = "limit") Integer limit,
                                 @RequestParam(name = "offset") Integer offset,
                                 @RequestParam(name = "rating") String rating,
                                 @RequestParam(name = "lang") String lan);

}
