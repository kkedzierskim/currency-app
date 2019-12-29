package com.nbp.currencyapp.service;

import com.nbp.currencyapp.converter.RateDTOtoCurrencyRate;
import com.nbp.currencyapp.dataloader.NbpRestService;
import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.ExchangeDTO;
import com.nbp.currencyapp.repository.CurrencyRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CurrencyServiceTest {

    private CurrencyService currencyService;

    @Mock
    private CurrencyRateRepository currencyRateRepository;
    @Mock
    private RateDTOtoCurrencyRate rateDTOtoCurrencyRate;
    @Mock
    private NbpRestService nbpRestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        currencyService = new CurrencyService(currencyRateRepository, rateDTOtoCurrencyRate, nbpRestService);
    }

    @Test
    void convertTest() {
        //given
        CurrencyRate PLNcurrencyRate = new CurrencyRate(1L, "polski złoty", "PLN", BigDecimal.ONE);
        CurrencyRate EURcurrencyRate = new CurrencyRate(2L, "euro", "EUR", BigDecimal.valueOf(4.28));
        //when
        when(currencyRateRepository.findByCode("PLN")).thenReturn(Optional.of(PLNcurrencyRate));
        when(currencyRateRepository.findByCode("EUR")).thenReturn(Optional.of(EURcurrencyRate));
        ExchangeDTO exchangeDTO = currencyService.convert(BigDecimal.ONE, "EUR", "PLN");
        //then
        assertEquals("EUR", exchangeDTO.getBaseCurrencyCode());
        assertEquals("PLN", exchangeDTO.getTargetCurrencyCode());
        assertEquals(BigDecimal.ONE, exchangeDTO.getAmount());
        assertEquals(BigDecimal.valueOf(4.28).setScale(4), exchangeDTO.getExchangeValue());
    }
}