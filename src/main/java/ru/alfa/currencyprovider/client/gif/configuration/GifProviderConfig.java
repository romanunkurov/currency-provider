package ru.alfa.currencyprovider.client.gif.configuration;

import feign.Feign;
import feign.Logger;
import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class GifProviderConfig {

    @Value("${ru.alfa.currencyprovider.connectTimeout}")
    private long connectTimeout;
    @Value("${ru.alfa.currencyprovider.readTimeout}")
    private long readTimeout;
    @Value("${ru.alfa.currencyprovider.followRedirects}")
    private Boolean followRedirects;

    @Bean
    public Feign.Builder fileFeignBuilder() {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Request.Options options = new Request.Options(connectTimeout, timeUnit, readTimeout, timeUnit, followRedirects);

        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .options(options);
    }
}
