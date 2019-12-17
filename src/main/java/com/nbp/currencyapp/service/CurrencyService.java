package com.nbp.currencyapp.service;

import com.nbp.currencyapp.dataloader.NbpRestService;
import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.ExchangeDTO;
import com.nbp.currencyapp.repository.CurrencyRateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class CurrencyService {

    private CurrencyRateRepository currencyRateRepository;

    public CurrencyService(NbpRestService nbpRestService, CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    public List<CurrencyRate> getAllCurrencyRates() {
        return currencyRateRepository.findAll();
    }


    public ExchangeDTO convert(BigDecimal amount, String baseCurrencyCode, String targetCurrencyCode) {

        BigDecimal currencyValue = findCurrencyExchange(targetCurrencyCode).divide(findCurrencyExchange(baseCurrencyCode), RoundingMode.CEILING);
        BigDecimal convertedAmount = currencyValue.multiply(amount);

        return new ExchangeDTO(amount, baseCurrencyCode, targetCurrencyCode, convertedAmount);
    }

    private BigDecimal findCurrencyExchange(String currencyCode) {

        Optional<CurrencyRate> currencyRateOptional = currencyRateRepository.findByCode(currencyCode);
        if (currencyRateOptional.isPresent()) {
            return currencyRateOptional.get().getExchange();
        } else throw new NoSuchElementException();
    }


    private CurrencyRate findCurrencyIsoCode(CurrencyRate userCurrencyRate) {
        Optional<CurrencyRate> currencyRateOptional = currencyRateRepository.findByCode(userCurrencyRate.getCode());
        if (currencyRateOptional.isPresent()) {
            return currencyRateOptional.get();
        } else throw new NoSuchElementException();
    }

    public List<CurrencyRate> getUserRateList(List<CurrencyRate> userCurrencyList) {
        List<CurrencyRate> resultList = new ArrayList<>();
        userCurrencyList.forEach(currencyRate -> resultList.add(findCurrencyIsoCode(currencyRate)));
        return resultList;
    }

}

