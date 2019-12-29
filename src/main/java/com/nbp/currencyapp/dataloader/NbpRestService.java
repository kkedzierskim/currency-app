package com.nbp.currencyapp.dataloader;

import com.nbp.currencyapp.dto.RatesTableDTO;
import com.nbp.currencyapp.service.MyHttpRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class NbpRestService {

    private final MyHttpRequestService myHttpRequestService;
    private static final String URL = "http://api.nbp.pl/api/exchangerates/tables/";

    public NbpRestService(MyHttpRequestService myHttpRequestService) {
        this.myHttpRequestService = myHttpRequestService;
    }

    public List<RatesTableDTO> loadExchangeRatesTable(NbpTableType nbpTableType) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<RatesTableDTO>> rateResponse;

        try {
            rateResponse = restTemplate.exchange(URL + nbpTableType.toString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<RatesTableDTO>>() {
                    });
        } catch (RestClientException ex) {
            throw new IllegalStateException("NBP convert rates api unavailable");
        }
        myHttpRequestService.saveRequest(URL + nbpTableType.toString(), LocalDateTime.now(), "GET");
        return rateResponse.getBody();

    }
}

