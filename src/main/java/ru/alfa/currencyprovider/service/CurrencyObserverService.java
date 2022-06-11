package ru.alfa.currencyprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.alfa.currencyprovider.client.exchangerate.ExchangeRateProvider;
import ru.alfa.currencyprovider.client.gif.GifProvider;
import ru.alfa.currencyprovider.dto.CurrencyDTO;
import ru.alfa.currencyprovider.dto.GifDTO;
import ru.alfa.currencyprovider.exception.BadRequestException;
import java.sql.Date;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrencyObserverService {

    private RestTemplate restTemplate;
    private ExchangeRateProvider exchangeRateProvider;
    private GifProvider gifProvider;

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

    public CurrencyObserverService(RestTemplate restTemplate, ExchangeRateProvider provider, GifProvider gifProvider) {
        this.restTemplate = restTemplate;
        this.exchangeRateProvider = provider;
        this.gifProvider = gifProvider;
    }

    /**
     * Метод получает и сравнивает последний и исторический курсы валюты по отношению к USD,
     * и отдает на фронт случайную GIF в зависимости от условий
     * @param ticker Тикер сравниваемой валюты
     * @return GIF (rich или broke)
     * @throws BadRequestException ошибка при невалидности входных данных
     */
    public byte[] getCurrencyCourse(String ticker) throws BadRequestException {

        //Получаем последний курс валют
        CurrencyDTO latestRate = getCurrencyLatest();

        // Получаем историческую сводку курса валют
        CurrencyDTO historicalRate = getCurrencyHistorical();

        Double latestValue = latestRate.getRates().get(ticker);
        Double historicalValue = historicalRate.getRates().get(ticker);

        if (latestValue != null
                && latestValue.isNaN()
                && historicalValue !=null
                && historicalValue.isNaN())
        {
            throw new ArithmeticException();
        }

        GifDTO gifDTO;

        // Сравниваем исторический и текущий курсы по отношению к доллару и если исторический больше, то отдаем broke Gif, иначе rich Gif
        if (latestValue > historicalValue) {
            gifDTO = gifProvider.getGif(gifToken, rich, limit, offset, rating, language);
        } else if (historicalValue > latestValue) {
            gifDTO = gifProvider.getGif(gifToken, broke, limit, offset, rating, language);
        } else {
            throw new BadRequestException("Невалидные данные, полученные ");
        }
        int randomInt = (int) (Math.random() * 19);
        String url = gifDTO.getData().get(randomInt).getImages().getDownsized().getUrl();

        return restTemplate.getForObject(url, byte[].class);
    }

    private CurrencyDTO getCurrencyLatest() {
        ResponseEntity<CurrencyDTO> response = exchangeRateProvider.getLatestRate(exchangeToken, currency);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpServerErrorException(response.getStatusCode(), "Ошибка при обращении к ");
        } else {
            CurrencyDTO currencyDTO = response.getBody();
            assert currencyDTO != null;
            return currencyDTO;
        }
    }

    private CurrencyDTO getCurrencyHistorical() {
        LocalDate date = LocalDate.now().minusDays(1);
        ResponseEntity<CurrencyDTO> response =  exchangeRateProvider.getHistoricalRate(Date.valueOf(date), exchangeToken, currency);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpServerErrorException(response.getStatusCode(), "Ошибка при обращении к сервису курса валют");
        } else {
            CurrencyDTO currencyDTO = response.getBody();
            assert currencyDTO != null;
            return currencyDTO;
        }
    }

    private GifDTO getConditionalGIF() {
        ResponseEntity<GifDTO> response = gifProvider.getGif(gifToken, rich, limit, offset, rating, language);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpServerErrorException(response.getStatusCode(), "Ошибка при обращении к gif провайдеру");
        } else {
            GifDTO gifDTO = response.getBody();
            assert gifDTO != null;
            return gifDTO;
        }
    }
}
