package com.example.TgBotMaven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class BaseApi {
    @Autowired
    RestTemplate restTemplate;


    protected <T> ResponseEntity<T> exchange(HttpMethod httpMethod, URI uri, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {

        return restTemplate.exchange(uri, httpMethod, requestEntity, responseType);
    }

    private <T> ResponseEntity<T> sendInJson(HttpMethod httpMethod, String url, Object bodyRequest, Map<String, ?> queryParameters, String authToken,ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = uriComponentsBuilder.build(false).toUri();
        HttpHeaders httpHeaders = provideDefaultHttpHeaders(authToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(bodyRequest, httpHeaders);
        return exchange(httpMethod, uri, requestEntity, responseType);
    }

    private static HttpHeaders provideDefaultHttpHeaders(String authToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if(authToken != null && !authToken.isEmpty()) {
            httpHeaders.set("Authorization", " " + authToken);
        }
        return httpHeaders;
    }

    protected <T> ResponseEntity<T> sendGetWithAuth(String url, Map<String, ?> queryParameters, ParameterizedTypeReference<T> responseType, String authToken)  {
        return sendInJson(HttpMethod.GET, url, null, queryParameters, authToken, responseType);
    }
}




