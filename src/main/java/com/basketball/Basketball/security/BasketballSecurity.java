package com.basketball.Basketball.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class BasketballSecurity {

    @Value("${headers.key}")
    private String headersKey;
    @Value("${headers.host}")
    private String headersHost;

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", headersKey);
        headers.set("X-RapidAPI-Host", headersHost);
        return headers;
    }


}
