package com.ajtraders.order.controller;

import com.ajtraders.order.dto.OrderDTO;
import com.ajtraders.order.dto.ResponseDto;
import com.ajtraders.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/say-hi")
    public String sayHi() {
        return "Hello from Order Service!";
    }

    @PostMapping("/add")
    public ResponseDto addOrder(@RequestBody @Valid OrderDTO orderDTO) {
        return orderService.addOrder(orderDTO);
    }

    @GetMapping("/getAll")
    public List<OrderDTO> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/getById/{id}")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseDto deleteById(@PathVariable Long id) {
        return orderService.deleteById(id);
    }

    @PostMapping("/update/{id}")
    public ResponseDto updateOrder(@PathVariable Long id, @RequestBody @Valid OrderDTO orderDTO) {
        return orderService.updateOrder(id, orderDTO);
    }
}
