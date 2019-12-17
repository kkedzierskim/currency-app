package com.nbp.currencyapp.dataloader;

import com.nbp.currencyapp.dto.RateDTO;
import com.nbp.currencyapp.dto.RatesTableDTO;
import com.nbp.currencyapp.service.MyHttpRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
public class RatesDataLoader {

    private final MyHttpRequestService myHttpRequestService;
    private List<RateDTO> rateDTOs = new ArrayList<>();
    private LocalDateTime lastUpdate;
    private static final String URL = "http://api.nbp.pl/api/exchangerates/tables/";

    public RatesDataLoader(MyHttpRequestService myHttpRequestService) {
        this.myHttpRequestService = myHttpRequestService;
    }

    @PostConstruct
    public void loadExchangeRates() {
        loadExchangeRatesTable(NbpTableType.A);
        loadExchangeRatesTable(NbpTableType.B);
    }

    private void loadExchangeRatesTable(NbpTableType tableType) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<RatesTableDTO>> rateResponse;

        try {
            rateResponse = restTemplate.exchange( URL + tableType.toString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<RatesTableDTO>>() {
                    });
        } catch (RestClientException ex) {
            throw new IllegalStateException("NBP convert rates api unavailable");
        }

        List<RatesTableDTO> ratesTableDTOs = rateResponse.getBody();
        if (!CollectionUtils.isEmpty(ratesTableDTOs)) {
            rateDTOs.addAll(ratesTableDTOs.get(0).getRates());
        }
        lastUpdate = LocalDateTime.now();
        myHttpRequestService.saveRequest(URL + tableType.toString(), LocalDateTime.now(), "GET");
    }

    private boolean reloadRatesIfNeeded() {
        return lastUpdate != null && LocalDateTime.now().isBefore(lastUpdate.plusDays(1));
    }

    public List<RateDTO> getRateDTOs() {
        if (!reloadRatesIfNeeded()) {
            rateDTOs.clear();
            log.info("Reloading currency rates");
            loadExchangeRates();
        }
        return rateDTOs;
    }
}

