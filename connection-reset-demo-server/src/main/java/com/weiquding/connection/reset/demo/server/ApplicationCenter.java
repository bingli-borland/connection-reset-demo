package com.weiquding.connection.reset.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * java.net.SocketException : Connection reset 测试服务端
 */
@SpringBootApplication
@RestController
public class ApplicationCenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCenter.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCenter.class, args);
    }

    @RequestMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String serverHello(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            LOGGER.info("headerName:[{}]==>{}", headerName, request.getHeader(headerName));
        }
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();
            LOGGER.info("parameterName:[{}]==>{}", parameterName, request.getParameter(parameterName));
        }
        return "server hello";
    }

}
