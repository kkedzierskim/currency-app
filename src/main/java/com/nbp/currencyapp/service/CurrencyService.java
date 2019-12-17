package com.nbp.currencyapp.service;

import com.nbp.currencyapp.dataloader.RatesDataLoader;
import com.nbp.currencyapp.dto.ExchangeDTO;
import com.nbp.currencyapp.dto.RateDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class CurrencyService {

   private RatesDataLoader ratesDataLoader;

    public CurrencyService(RatesDataLoader ratesDataLoader) {
        this.ratesDataLoader = ratesDataLoader;
    }

    public List<RateDTO> getAllRates() {
       return ratesDataLoader.getRateDTOs();
    }


    public ExchangeDTO convert(BigDecimal amount, String baseCurrencyCode, String targetCurrencyCode) {

        BigDecimal currencyValue = findCurrencyMid(targetCurrencyCode).divide(findCurrencyMid(baseCurrencyCode), RoundingMode.CEILING);
        BigDecimal convertedAmount = currencyValue.multiply(amount);

        return new ExchangeDTO(amount, baseCurrencyCode, targetCurrencyCode, convertedAmount);
    }

    private BigDecimal findCurrencyMid(String currencyCode) {
        Optional<BigDecimal> currencyMidOptional = getAllRates().stream()
                .filter(currency -> currency.getCode().equals(currencyCode))
                .findFirst()
                .map(currency -> BigDecimal.valueOf(currency.getMid()));

        if (currencyMidOptional.isPresent()) {
            return currencyMidOptional.get();
        } else throw new NoSuchElementException();
    }

    private RateDTO findCurrencyIsoCode(RateDTO userRateDTO) {
        Optional<RateDTO> rateDTOOptional = getAllRates().stream()
                .filter(currency -> currency.getCode().equals(userRateDTO.getCode()))
                .findFirst();

        if (rateDTOOptional.isPresent()) {
            return rateDTOOptional.get();
        } else throw new NoSuchElementException();
    }

    public List<RateDTO> getListRates(List<RateDTO> userRateDTOList){
        List<RateDTO> resultList = new ArrayList<>();
        userRateDTOList.forEach(rateDTO -> resultList.add(findCurrencyIsoCode(rateDTO)));
        return resultList;
    }

}

