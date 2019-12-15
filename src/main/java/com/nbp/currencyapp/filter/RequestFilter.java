package com.nbp.currencyapp.filter;

import com.nbp.currencyapp.service.MyHttpRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class RequestFilter implements Filter {

    private final MyHttpRequestService myHttpRequestService;

    public RequestFilter(MyHttpRequestService myHttpRequestService) {
        this.myHttpRequestService = myHttpRequestService;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        log.info("Logged : " + req.getMethod() + req.getRequestURI());
        myHttpRequestService.saveRequest(req.getRequestURI(), LocalDateTime.now(), req.getMethod());
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}