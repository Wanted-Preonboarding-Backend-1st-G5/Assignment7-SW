package com.project.cardoc.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  public static final int READ_TIME_OUT = 5000;
  public static final int CONNECT_TIME_OUT = 3000;
  public static final int MAX_CONNECTION_TOTAL = 50;
  public static final int MAX_CONNECTION_PER_ROUTE = 20;

  @Bean
  public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

    factory.setReadTimeout(READ_TIME_OUT);
    factory.setConnectTimeout(CONNECT_TIME_OUT);

    // Apache HttpComponents HttpClient
    HttpClient httpClient = HttpClientBuilder.create()
        .setMaxConnTotal(MAX_CONNECTION_TOTAL)    // 최대 커넥션 수
        .setMaxConnPerRoute(MAX_CONNECTION_PER_ROUTE) // 각 호스트(IP & Port의 조합)당 커넥션 풀에 생성 가능한 커넥션 수
        .build();

    factory.setHttpClient(httpClient);

    return new RestTemplate(factory);
  }

}
