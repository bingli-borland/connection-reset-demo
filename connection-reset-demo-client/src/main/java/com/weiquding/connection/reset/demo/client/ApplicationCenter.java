package com.weiquding.connection.reset.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * java.net.SocketException : Connection reset 测试客户端
 */
@SpringBootApplication
@RestController
public class ApplicationCenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCenter.class);

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCenter.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    @RequestMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String serverHello(@RequestParam("key")String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("key", key);
        map.add("requestId", UUID.randomUUID().toString());
        map.add("timestamp", String.valueOf(System.currentTimeMillis()));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String value = restTemplate.postForObject("http://192.168.0.103:8083/hello", request, String.class);
        LOGGER.info("receive value:[{}]", value);
        return value;
    }


}
