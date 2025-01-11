package com.ajtraders.order;

import com.AJTraders.ProductClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/order-hi")
public class GreetingsController {

    private final ProductClient productClient;
    @GetMapping("/say-hi")
    public String sayHi() {
      log.info("hello"+productClient.getProducts());

        return "Hello from order Service!";
    }
}
