package com.nbp.currencyapp.service;


import com.nbp.currencyapp.dao.CurrenciesDAO;
import com.nbp.currencyapp.dao.ExchangeDAO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    CurrenciesService currenciesService;

    public ExchangeServiceImpl(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @Override
    public ExchangeDAO convert(BigDecimal amount, String baseCurrencyCode, String targetCurrencyCode) {

        Optional<BigDecimal> baseCurrencyRateOptional = currenciesService.getCurrencies()
                .stream()
                .filter(currency -> currency.getISOcode().equals(baseCurrencyCode))
                .findFirst()
                .map(currency -> currency.getMid());

        Optional<BigDecimal> targetCurrencyRateOptional = currenciesService.getCurrencies()
                .stream()
                .filter(currency -> currency.getISOcode().equals(targetCurrencyCode))
                .findFirst()
                .map(currency -> currency.getMid());


        BigDecimal currencyValue = targetCurrencyRateOptional.get().divide(baseCurrencyRateOptional.get(), RoundingMode.CEILING);
        BigDecimal convertedAmount = currencyValue.multiply(amount);

        ExchangeDAO exchangeDAO = new ExchangeDAO(amount, baseCurrencyCode, targetCurrencyCode, convertedAmount);
        return exchangeDAO;
    }
}
