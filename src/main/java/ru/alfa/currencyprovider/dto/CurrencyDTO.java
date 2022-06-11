package ru.alfa.currencyprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "currency", description = "ДТО валют")
public class CurrencyDTO {

    private String disclaimer;
    private String licence;
    private Date timestamp;
    private String base;
    private Map<String, Double> rates;
}
