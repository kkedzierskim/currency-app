package com.nbp.currencyapp.controller;

import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.ExchangeDTO;
import com.nbp.currencyapp.repository.CurrencyRateRepository;
import com.nbp.currencyapp.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CurrencyControllerTest {

    private CurrencyController currencyController;
    private MockMvc mockMvc;

    @Mock
    CurrencyService currencyService;

    @Mock
    CurrencyRateRepository currencyRateRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        currencyController = new CurrencyController(currencyService);
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController)
                .build();
    }

    @Test
    void getCurrenciesTest() throws Exception{
        //given
        CurrencyRate PLN = new CurrencyRate("polski złoty", "PLN", BigDecimal.ONE);
        CurrencyRate EUR = new CurrencyRate("euro", "EUR", BigDecimal.valueOf(4.28));
        List<CurrencyRate> currencies = new ArrayList<>();
        currencies.add(PLN);
        currencies.add(EUR);
        //when
        when(currencyService.getAllCurrencyRates()).thenReturn(currencies);
        //then
        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(currencyService.getAllCurrencyRates().size(), 2);
    }

    @Test
    void getExchange() throws Exception{
        //given
        CurrencyRate PLN = new CurrencyRate("polski złoty", "PLN", BigDecimal.ONE);
        CurrencyRate EUR = new CurrencyRate("euro", "EUR", BigDecimal.valueOf(4.28));
        //when
        when(currencyRateRepository.findByCode(PLN.getCode())).thenReturn(Optional.of(PLN));
        when(currencyRateRepository.findByCode(EUR.getCode())).thenReturn(Optional.of(EUR));
        when(currencyService.convert(BigDecimal.ONE, PLN.getCode(), EUR.getCode()))
                .thenReturn(new ExchangeDTO(BigDecimal.ONE, PLN.getCode(), EUR.getCode(), BigDecimal.valueOf(4.28)));
        ExchangeDTO exchange = currencyService.convert(BigDecimal.ONE, PLN.getCode(), EUR.getCode());

        //then
        mockMvc.perform(get("/currency/convert?amount=1&baseCurrency=PLN&targetCurrency=EUR"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(exchange.getExchangeValue(), BigDecimal.valueOf(4.28));
    }
}