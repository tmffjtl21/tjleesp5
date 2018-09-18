package com.tjleesp5.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 *  토비의봄 12회 - SpringBoot 2.0 M1, SpringFrameWork 5.0 RC1
 * */
@Slf4j
@SpringBootApplication
@EnableAsync
public class Toby012 {
    @RestController
    public static class MyController {
        static final String URL1 = "http://localhost:8081/service?req={req}";
        static final String URL2 = "http://localhost:8081/service2?req={req}";

        @Autowired
        MyService myService;

        // Spring Web Flux 에서는 Mono 와 Flux 두가지로 리턴하면 된다.
        // Mono는 파라미터를 한번에 던지고 결과를 한번에 담고 그런스타일을 유사하게 만들 수있다.


        WebClient client = WebClient.create();      // 여러 쓰레드가 동시에 재사용이 가능함 , 기존의 AsyncRestTemplete 과 유사한 거라고 생각하면됨.

        @GetMapping("/rest")
        public Mono<String> rest(int idx) throws InterruptedException {
            log.error("test");
            // 일더 스타일로 작성하게 돼있음
            // reactive 스타일로 정의한 것, 이렇게만 정의하면 실행이 되지않음.
            // 퍼블리셔를 상속받음. 누가 구독을 하지않으면 데이터를 만들지 않음
            // 퍼블리셔는 서브스크라이버가 서브스크라입 하기전에는 실행되지 않음 
//            Mono<ClientResponse> res = client.get().uri(URL1, idx).exchange();
//            Mono<String> body = res.flatMap(clientResponse -> clientResponse.bodyToMono(String.class)); 한줄로

            return client.get().uri(URL1, idx).exchange()
                    .flatMap(c -> c.bodyToMono(String.class));
//                    .flatMap(res1 -> client.get().uri(URL2, res1).exchange())
//                    .flatMap(c -> c.bodyToMono(String.class))
//                    .doOnNext(c -> log.info(c))
//                    .flatMap(res2 -> Mono.fromCompletionStage(myService.work(res2)));
                                        // CompletableFuture<String> => Mono<String>

//            return Mono.fromCompletionStage(myService.work(String.valueOf(idx)));

        }
    }

    @Service
    @Async
    public static class MyService {
        public CompletableFuture<String> work(String req){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            log.error(req + "asyncwork");
            return CompletableFuture.completedFuture(req + "/asyncwork");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Toby012.class, args);
    }
}