package com.nbp.currencyapp.service;

import com.nbp.currencyapp.domain.MyHttpRequest;
import com.nbp.currencyapp.repository.MyHttpRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MyHttpRequestService {


    private final MyHttpRequestRepository myHttpRequestRepository;

    public MyHttpRequestService(MyHttpRequestRepository myHttpRequestRepository) {
        this.myHttpRequestRepository = myHttpRequestRepository;
    }

    public void saveRequest(String url, LocalDateTime requestDateTime, String requestMethod){
        myHttpRequestRepository.save(
                MyHttpRequest.builder().requestDateTime(requestDateTime)
                .requestMethod(requestMethod)
                .url(url)
                .build()
        );
    }

}
