package com.nbp.currencyapp.service;

import com.nbp.currencyapp.dao.ExchangeDAO;

import java.math.BigDecimal;

public interface ExchangeService {

    ExchangeDAO convert(BigDecimal amount, String baseCurrencyCode, String targetCurrencyCode);


}
