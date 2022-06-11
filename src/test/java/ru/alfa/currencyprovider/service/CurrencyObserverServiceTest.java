package ru.alfa.currencyprovider.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.alfa.currencyprovider.client.exchangerate.ExchangeRateProvider;
import ru.alfa.currencyprovider.client.gif.GifProvider;
import ru.alfa.currencyprovider.dto.CurrencyDTO;
import ru.alfa.currencyprovider.dto.GifDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyObserverServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ExchangeRateProvider exchangeRateProvider;
    @Mock
    private GifProvider gifProvider;
    @InjectMocks
    private CurrencyObserverService service;

    byte[] brokeGifArray = Files.readAllBytes(Paths.get("src/test/resources/broke.gif"));
    byte[] richGifArray = Files.readAllBytes(Paths.get("src/test/resources/rich.gif"));

    public CurrencyObserverServiceTest() throws IOException {
    }

    @BeforeEach
    public void setup() {

        ReflectionTestUtils.setField(service, "exchangeToken", "token");
        ReflectionTestUtils.setField(service, "currency", "USD");
        ReflectionTestUtils.setField(service, "gifToken", "token");
        ReflectionTestUtils.setField(service, "language", "en");
        ReflectionTestUtils.setField(service, "offset", 10);
        ReflectionTestUtils.setField(service, "broke", "broke");
        ReflectionTestUtils.setField(service, "rich", "rich");
        ReflectionTestUtils.setField(service, "limit", 10);
        ReflectionTestUtils.setField(service, "rating", "g");
    }

    @Test
    public void getCurrencyCourse_basicBroke() {
        CurrencyDTO latestCurrency = new CurrencyDTO();
        Map<String, Double> lRates = new HashMap<>();
        lRates.put("RUB", 59.222);
        latestCurrency.setRates(lRates);

        CurrencyDTO historicalCurrency = new CurrencyDTO();
        Map<String, Double> hRates = new HashMap<>();
        hRates.put("RUB", 60.331);
        historicalCurrency.setRates(hRates);

        GifDTO gifDTO = new GifDTO();
        List<GifDTO.Gif> gifs = new ArrayList<>();
        gifDTO.setData(gifs);

        GifDTO.Gif gif = new GifDTO.Gif();
        gif.setImages(new GifDTO.Gif.Images());
        gif.getImages().setDownsized(new GifDTO.Gif.Images.Downsized());
        gif.getImages().getDownsized().setUrl("urlToDownloadBroke");
        for (int i = 0; i < 20; i++) {
            gifs.add(gif);
        }

        when(restTemplate.getForObject("urlToDownloadBroke", byte[].class)).thenReturn(brokeGifArray);
        when(exchangeRateProvider.getHistoricalRate(any(), anyString(), anyString())).thenReturn(ResponseEntity.ok(historicalCurrency));
        when(exchangeRateProvider.getLatestRate(anyString(), anyString())).thenReturn(ResponseEntity.ok(latestCurrency));

        when(gifProvider.getGif("token", "broke", 10, 10, "g", "en")).thenReturn(ResponseEntity.ok(gifDTO));
        //when(gifProvider.getGif("token", "rich", 10, 10, "g", "en")).thenReturn(ResponseEntity.ok(gifDTO));

        byte[] gifBrokeByteArray = service.getCurrencyCourse("RUB");

        assertEquals(gifBrokeByteArray, brokeGifArray);
    }

    @Test
    public void getCurrencyCourse_basicRich() {
        CurrencyDTO latestCurrency = new CurrencyDTO();
        Map<String, Double> lRates = new HashMap<>();
        lRates.put("RUB", 69.222);
        latestCurrency.setRates(lRates);

        CurrencyDTO historicalCurrency = new CurrencyDTO();
        Map<String, Double> hRates = new HashMap<>();
        hRates.put("RUB", 50.331);
        historicalCurrency.setRates(hRates);

        GifDTO gifDTO = new GifDTO();
        List<GifDTO.Gif> gifs = new ArrayList<>();
        gifDTO.setData(gifs);

        GifDTO.Gif gif = new GifDTO.Gif();
        gif.setImages(new GifDTO.Gif.Images());
        gif.getImages().setDownsized(new GifDTO.Gif.Images.Downsized());
        gif.getImages().getDownsized().setUrl("urlToDownloadRich");
        for (int i = 0; i < 20; i++) {
            gifs.add(gif);
        }

        when(restTemplate.getForObject("urlToDownloadRich", byte[].class)).thenReturn(richGifArray);
        when(exchangeRateProvider.getHistoricalRate(any(), anyString(), anyString())).thenReturn(ResponseEntity.ok(historicalCurrency));
        when(exchangeRateProvider.getLatestRate(anyString(), anyString())).thenReturn(ResponseEntity.ok(latestCurrency));

        //when(gifProvider.getGif("token", "broke", 10, 10, "g", "en")).thenReturn(ResponseEntity.ok(gifDTO));
        when(gifProvider.getGif("token", "rich", 10, 10, "g", "en")).thenReturn(ResponseEntity.ok(gifDTO));

        byte[] gifRichByteArray = service.getCurrencyCourse("RUB");

        assertEquals(gifRichByteArray, richGifArray);
    }

    @Test
    public void getCurrencyCourse_exchange_rate_exception_handler() {
        CurrencyDTO latestCurrency = new CurrencyDTO();
        Map<String, Double> lRates = new HashMap<>();
        lRates.put("RUB", 69.222);
        latestCurrency.setRates(lRates);

        CurrencyDTO historicalCurrency = new CurrencyDTO();
        Map<String, Double> hRates = new HashMap<>();
        hRates.put("RUB", 50.331);
        historicalCurrency.setRates(hRates);

        GifDTO gifDTO = new GifDTO();
        List<GifDTO.Gif> gifs = new ArrayList<>();
        gifDTO.setData(gifs);

        GifDTO.Gif gif = new GifDTO.Gif();
        gif.setImages(new GifDTO.Gif.Images());
        gif.getImages().setDownsized(new GifDTO.Gif.Images.Downsized());
        gif.getImages().getDownsized().setUrl("urlToDownloadRich");
        for (int i = 0; i < 20; i++) {
            gifs.add(gif);
        }

        when(exchangeRateProvider.getHistoricalRate(any(), anyString(), anyString())).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        when(exchangeRateProvider.getLatestRate(anyString(), anyString())).thenReturn(ResponseEntity.ok(latestCurrency));

        assertThrows(HttpServerErrorException.class, () -> service.getCurrencyCourse("RUB"));
    }

    @Test
    public void getCurrencyCourse_gif_exception_handler2() {
        CurrencyDTO latestCurrency = new CurrencyDTO();
        Map<String, Double> lRates = new HashMap<>();
        lRates.put("RUB", 69.222);
        latestCurrency.setRates(lRates);

        CurrencyDTO historicalCurrency = new CurrencyDTO();
        Map<String, Double> hRates = new HashMap<>();
        hRates.put("RUB", 50.331);
        historicalCurrency.setRates(hRates);

        GifDTO gifDTO = new GifDTO();
        List<GifDTO.Gif> gifs = new ArrayList<>();
        gifDTO.setData(gifs);

        GifDTO.Gif gif = new GifDTO.Gif();
        gif.setImages(new GifDTO.Gif.Images());
        gif.getImages().setDownsized(new GifDTO.Gif.Images.Downsized());
        gif.getImages().getDownsized().setUrl("urlToDownloadRich");
        for (int i = 0; i < 20; i++) {
            gifs.add(gif);
        }

        when(exchangeRateProvider.getHistoricalRate(any(), anyString(), anyString())).thenReturn(ResponseEntity.ok(historicalCurrency));
        when(exchangeRateProvider.getLatestRate(anyString(), anyString())).thenReturn(ResponseEntity.ok(latestCurrency));


        when(gifProvider.getGif("token", "rich", 10, 10, "g", "en")).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        assertThrows(HttpServerErrorException.class, () -> service.getCurrencyCourse("RUB"));
    }
}
