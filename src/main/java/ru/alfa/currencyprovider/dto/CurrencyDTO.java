package ru.alfa.currencyprovider.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class CurrencyDTO {
    private String disclaimer;
    private String licence;
    private Date timestamp;
    private String base;
    private Map<String, Double> rates;
}
