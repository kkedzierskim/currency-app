package com.nbp.currencyapp.controller;

import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.ExchangeDTO;
import com.nbp.currencyapp.repository.CurrencyRateRepository;
import com.nbp.currencyapp.service.CurrencyService;
import org.hamcrest.Matchers;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void getCurrenciesTest() throws Exception {
        //given
        CurrencyRate PLN = new CurrencyRate(1L, "polski złoty", "PLN", BigDecimal.ONE);
        CurrencyRate EUR = new CurrencyRate(2L, "euro", "EUR", BigDecimal.valueOf(4.28));
        List<CurrencyRate> currencies = new ArrayList<>();
        currencies.add(PLN);
        currencies.add(EUR);
        //when
        when(currencyService.getAllCurrencyRates()).thenReturn(currencies);
        //then
        mockMvc.perform(get("/currencies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].code", Matchers.is("PLN")))
                .andExpect(jsonPath("$[0].exchange", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[1].code", Matchers.is("EUR")))
                .andExpect(jsonPath("$[1].exchange", Matchers.is(4.28)));
        assertEquals(currencyService.getAllCurrencyRates().size(), 2);
    }

    @Test
    void getExchange() throws Exception {
        //given
        CurrencyRate PLN = new CurrencyRate("polski złoty", "PLN", BigDecimal.ONE);
        CurrencyRate EUR = new CurrencyRate("euro", "EUR", BigDecimal.valueOf(4.28));
        //when
        when(currencyService.convert(any(), eq(EUR.getCode()), eq(PLN.getCode())))
                .thenReturn(new ExchangeDTO(BigDecimal.ONE, EUR.getCode(), PLN.getCode(), BigDecimal.valueOf(4.28)));
        //then
        mockMvc.perform(get("/currency/convert?amount=1&baseCurrency=EUR&targetCurrency=PLN")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", Matchers.is(1)))
                .andExpect(jsonPath("$.baseCurrencyCode", Matchers.is("EUR")))
                .andExpect(jsonPath("$.targetCurrencyCode", Matchers.is("PLN")))
                .andExpect(jsonPath("$.exchangeValue", Matchers.is(4.28)));
    }
}