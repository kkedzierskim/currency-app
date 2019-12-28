package com.nbp.currencyapp.service;

import com.nbp.currencyapp.converter.RateDTOtoCurrencyRate;
import com.nbp.currencyapp.dataloader.NbpRestService;
import com.nbp.currencyapp.dataloader.NbpTableType;
import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.ExchangeDTO;
import com.nbp.currencyapp.dto.RatesTableDTO;
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
    private RateDTOtoCurrencyRate rateDTOtoCurrencyRate;
    private NbpRestService nbpRestService;

    public CurrencyService(CurrencyRateRepository currencyRateRepository,
                           RateDTOtoCurrencyRate rateDTOtoCurrencyRate, NbpRestService nbpRestService) {
        this.currencyRateRepository = currencyRateRepository;
        this.rateDTOtoCurrencyRate = rateDTOtoCurrencyRate;
        this.nbpRestService = nbpRestService;
    }

    public List<CurrencyRate> getAllCurrencyRates() {
        return currencyRateRepository.findAll();
    }


    public ExchangeDTO convert(BigDecimal amount, String baseCurrencyCode, String targetCurrencyCode) {

        BigDecimal currencyValue = findCurrencyExchange(baseCurrencyCode)
                .divide(findCurrencyExchange(targetCurrencyCode), 4, RoundingMode.CEILING);
        BigDecimal convertedAmount = currencyValue.multiply(amount);

        return new ExchangeDTO(amount, baseCurrencyCode, targetCurrencyCode,
                convertedAmount.setScale(4, RoundingMode.CEILING));
    }

    private BigDecimal findCurrencyExchange(String currencyCode) {

        Optional<CurrencyRate> currencyRateOptional = currencyRateRepository.findByCode(currencyCode);
        if (currencyRateOptional.isPresent()) {
            return currencyRateOptional.get().getExchange();
        } else throw new NoSuchElementException();
    }


    public void saveOrUpdateData() {
        List<RatesTableDTO> ratesTableDTOs = new ArrayList<>();
        ratesTableDTOs.addAll(nbpRestService.loadExchangeRatesTable(NbpTableType.A));
        ratesTableDTOs.addAll(nbpRestService.loadExchangeRatesTable(NbpTableType.B));

        if (currencyRateRepository.findAll().isEmpty()) {
            currencyRateRepository.save(new CurrencyRate("polski złoty", "PLN", BigDecimal.ONE));
            ratesTableDTOs.stream()
                    .map(RatesTableDTO::getRates)
                    .flatMap(List::stream)
                    .map(rateDTOtoCurrencyRate::convert)
                    .forEach(currencyRateRepository::save);
        } else {
            ratesTableDTOs.stream()
                    .map(RatesTableDTO::getRates)
                    .flatMap(List::stream)
                    .map(rateDTOtoCurrencyRate::convert)
                    .forEach(currencyRate -> {
                        if (currencyRateRepository.findByCode(currencyRate.getCode()).isPresent()) {
                            currencyRateRepository.updateExchange(currencyRate.getCode(), currencyRate.getExchange());
                        } else
                            currencyRateRepository.save(currencyRate);
                    });

        }
    }
}

