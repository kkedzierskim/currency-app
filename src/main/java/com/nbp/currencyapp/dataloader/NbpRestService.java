package com.nbp.currencyapp.dataloader;

import com.nbp.currencyapp.converter.RateDTOtoCurrencyRate;
import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.RateDTO;
import com.nbp.currencyapp.dto.RatesTableDTO;
import com.nbp.currencyapp.repository.CurrencyRateRepository;
import com.nbp.currencyapp.service.MyHttpRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NbpRestService {

    private final MyHttpRequestService myHttpRequestService;
    private final RateDTOtoCurrencyRate rateDTOtoCurrencyRate;
    private final CurrencyRateRepository currencyRateRepository;
    private static final String URL = "http://api.nbp.pl/api/exchangerates/tables/";

    public NbpRestService(MyHttpRequestService myHttpRequestService, RateDTOtoCurrencyRate rateDTOtoCurrencyRate, CurrencyRateRepository currencyRateRepository) {
        this.myHttpRequestService = myHttpRequestService;
        this.rateDTOtoCurrencyRate = rateDTOtoCurrencyRate;
        this.currencyRateRepository = currencyRateRepository;
    }

    @PostConstruct
    public void loadExchangeRates() {
        loadExchangeRatesTable(NbpTableType.A);
    }

    @Scheduled(fixedDelayString = "${updateRatesData.delay}")
    private void scheduledUpdateOfCurrencyRates(){
        loadExchangeRates();
    }

    private void loadExchangeRatesTable(NbpTableType nbpTableType) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<RatesTableDTO>> rateResponse;

        try {
            rateResponse = restTemplate.exchange(URL + nbpTableType.toString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<RatesTableDTO>>() {
                    });
        } catch (RestClientException ex) {
            throw new IllegalStateException("NBP convert rates api unavailable");
        }

        List<RateDTO> rateDTOs = new ArrayList<>();
        List<RatesTableDTO> ratesTableDTOs = rateResponse.getBody();
        if (!CollectionUtils.isEmpty(ratesTableDTOs)) {
            rateDTOs.addAll(ratesTableDTOs.get(0).getRates());
        }

        if (currencyRateRepository.findAll().isEmpty()) {
            initialSaveData(rateDTOs);
        } else updateExchangeRatesData(rateDTOs);

        myHttpRequestService.saveRequest(URL + nbpTableType.toString(), LocalDateTime.now(), "GET");
    }

    private void initialSaveData(List<RateDTO> rateDTOs) {
        rateDTOs.stream()
                .map(rateDTOtoCurrencyRate::convert)
                .forEach(currencyRateRepository::save);
    }

    void updateExchangeRatesData(List<RateDTO> rateDTOs) {
        log.info("updating exchange rates");
        rateDTOs.stream()
                .map(rateDTOtoCurrencyRate::convert)
                .forEach(currencyRate -> {
                    if(currencyRateRepository.findByCode(currencyRate.getCode()).isPresent()) {
                        currencyRateRepository.updateExchange(currencyRate.getCode(), currencyRate.getExchange());
                    }
                    else
                        currencyRateRepository.save(currencyRate);
                });
    }
}

