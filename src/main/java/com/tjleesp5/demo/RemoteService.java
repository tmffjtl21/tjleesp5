/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tjleesp5.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

// 백단의 어떤 서비스라고 생각함 ( 톰캣이 하나 더 떠있는거 )
@SpringBootApplication
@Slf4j
public class RemoteService {
    @RestController
    public static class MyController{
        @GetMapping("/service")
        public Mono<String> service(String req) throws InterruptedException {

            log.info("Service1");
            Thread.sleep(1000);
            return Mono.just(req + "/service");
        }

        @GetMapping("/service2")
        public Mono<String> service2(String req) throws InterruptedException {
            log.info("Service2");
            Thread.sleep(1000);
            return Mono.just(req + "/service2");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(RemoteService.class, args);
    }
}
