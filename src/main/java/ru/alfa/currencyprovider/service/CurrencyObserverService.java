package ru.alfa.currencyprovider.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.alfa.currencyprovider.client.exchangerate.ExchangeRateProvider;
import ru.alfa.currencyprovider.client.gif.GifProvider;
import ru.alfa.currencyprovider.dto.CurrencyDTO;
import ru.alfa.currencyprovider.dto.GifDTO;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

@Service
@Slf4j
public class CurrencyObserverService {

    private final RestTemplate restTemplate;
    private final ExchangeRateProvider exchangeRateProvider;
    private final GifProvider gifProvider;

    public CurrencyObserverService(RestTemplate restTemplate, ExchangeRateProvider exchangeRateProvider, GifProvider gifProvider) {
        this.restTemplate = restTemplate;
        this.exchangeRateProvider = exchangeRateProvider;
        this.gifProvider = gifProvider;
    }

    @Value("${ru.alfa.currencyprovider.exchange.token}")
    private String exchangeToken;
    @Value("${ru.alfa.currencyprovider.exchange.base}")
    private String currency;
    @Value("${ru.alfa.currencyprovider.gif.token}")
    private String gifToken;
    @Value("${ru.alfa.currencyprovider.gif.language}")
    private String language;
    @Value("${ru.alfa.currencyprovider.gif.offset}")
    private Integer offset;
    @Value("${ru.alfa.currencyprovider.gif.query.broke}")
    private String broke;
    @Value("${ru.alfa.currencyprovider.gif.query.rich}")
    private String rich;
    @Value("${ru.alfa.currencyprovider.gif.limit}")
    private Integer limit;
    @Value("${ru.alfa.currencyprovider.gif.rating}")
    private String rating;

    /**
     * Метод получает и сравнивает последний и исторический курсы валюты по отношению к USD,
     * и отдает на фронт случайную GIF в зависимости от условий
     *
     * @param ticker Тикер сравниваемой валюты
     * @return GIF (rich или broke)
     */
    public byte[] getCurrencyCourse(String ticker) {

        //Получаем последний курс валют
        CurrencyDTO latestRate = getCurrencyResponse(false);

        // Получаем историческую сводку курса валют
        CurrencyDTO historicalRate = getCurrencyResponse(true);

        Double latestValue = latestRate.getRates().get(ticker);
        Double historicalValue = historicalRate.getRates().get(ticker);

        if (latestValue != null
                && latestValue.isNaN()
                && historicalValue != null
                && historicalValue.isNaN()) {
            throw new ArithmeticException();
        }
        GifDTO gifDTO;

        log.info(latestValue + " " + historicalValue);

        // Сравниваем исторический и текущий курсы по отношению к доллару и если исторический больше, то отдаем broke Gif, иначе rich Gif
        if (latestValue > historicalValue) {
            gifDTO = getConditionalGIF(rich);
        } else if (historicalValue > latestValue) {
            gifDTO = getConditionalGIF(broke);
        } else {
            throw new RuntimeException("Невалидные данные, полученные");
        }
        int randomInt = (int) (Math.random() * 19);
        String url = gifDTO.getData().get(randomInt).getImages().getDownsized().getUrl();

        return restTemplate.getForObject(url, byte[].class);
    }

    private CurrencyDTO getCurrencyResponse(boolean isHistorical) {
        LocalDate date = getValidityDate();
        ResponseEntity<CurrencyDTO> response;
        if (isHistorical) {
            response = exchangeRateProvider.getHistoricalRate(Date.valueOf(date), exchangeToken, currency);
            log.info("into historical IF" + " " + response.getBody());
        } else {
            response = exchangeRateProvider.getLatestRate(exchangeToken, currency);
            log.info("into latest if " + response.getBody());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpServerErrorException(response.getStatusCode(), "Ошибка при обращении к сервису курса валют");
        } else {
            CurrencyDTO currencyDTO = response.getBody();
            assert currencyDTO != null;
            return currencyDTO;
        }
    }

    private GifDTO getConditionalGIF(String state) {
        ResponseEntity<GifDTO> response = gifProvider.getGif(gifToken, state, limit, offset, rating, language);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpServerErrorException(response.getStatusCode(), "Ошибка при обращении к gif провайдеру");
        } else {
            GifDTO gifDTO = response.getBody();
            assert gifDTO != null;
            return gifDTO;
        }
    }

    private LocalDate getValidityDate() {
        DayOfWeek day = DayOfWeek.of(LocalDate.now().get(ChronoField.DAY_OF_WEEK));
        if (day == DayOfWeek.SATURDAY) {
            return LocalDate.now().minusDays(2);
        } else if (day == DayOfWeek.SUNDAY) {
            return LocalDate.now().minusDays(3);
        } else {
            return LocalDate.now().minusDays(1);
        }
    }
}
