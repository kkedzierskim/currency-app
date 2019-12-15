package com.nbp.currencyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeDTO {

    private BigDecimal amount;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal exchangeValue;
}
