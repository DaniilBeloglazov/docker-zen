package com.example.shell.config;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {
    private final RestTemplateInterceptor restTemplateInterceptor;
    @Bean
    public RestTemplate restTemplateBean(){
        RestTemplate restTemplate = new RestTemplate();
        //interceptor config
        List<ClientHttpRequestInterceptor> interceptors
                = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(restTemplateInterceptor);
        restTemplate.setInterceptors(interceptors);
        // http client config
        val httpclient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpclient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
