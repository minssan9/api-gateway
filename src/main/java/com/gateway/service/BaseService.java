package com.gateway.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BaseService {
    private WebClient webClient = WebClient.builder()
        .baseUrl("http://localhost:35000").build();

//    @Hys
}
