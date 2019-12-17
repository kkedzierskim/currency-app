package com.nbp.currencyapp.repository;

import com.nbp.currencyapp.domain.MyHttpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface MyHttpRequestRepository extends JpaRepository<MyHttpRequest, Long> {

}
