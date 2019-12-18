package com.nbp.currencyapp.repository;

import com.nbp.currencyapp.domain.MyHttpRequestLoger;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MyHttpRequestRepository extends JpaRepository<MyHttpRequestLoger, Long> {

}
