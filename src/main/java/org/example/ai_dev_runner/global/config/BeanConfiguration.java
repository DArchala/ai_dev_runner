package org.example.ai_dev_runner.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
public class BeanConfiguration {

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                         .build();
    }

    @Bean
    HttpClient httpClient() {
        return HttpClient.newBuilder()
                         .followRedirects(HttpClient.Redirect.ALWAYS)
                         .build();
    }

}
