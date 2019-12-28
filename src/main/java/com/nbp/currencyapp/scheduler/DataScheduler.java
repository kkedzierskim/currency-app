package com.nbp.currencyapp.scheduler;

import com.nbp.currencyapp.service.CurrencyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataScheduler {

   private final CurrencyService currencyService;

    public DataScheduler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Scheduled(initialDelay = 0, fixedDelayString = "${updateRatesData.delay}")
    private void scheduledUpdateOfCurrencyRates(){
        currencyService.saveOrUpdateData();
    }
}
