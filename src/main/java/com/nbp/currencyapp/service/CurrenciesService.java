package com.nbp.currencyapp.service;

import com.nbp.currencyapp.dao.CurrenciesDAO;
import com.nbp.currencyapp.domain.Currency;
import com.nbp.currencyapp.domain.NbpTableType;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class CurrenciesService {

    private List<Currency> currencies = new ArrayList<>();
    private final CurrenciesDAO currenciesDAO;


    public CurrenciesService(CurrenciesDAO currenciesDAO) {
        this.currenciesDAO = currenciesDAO;
    }

    public List<Currency> getCurrencies() {
        currencies = currenciesDAO.parse(NbpTableType.A);
        return currencies;
    }
}

