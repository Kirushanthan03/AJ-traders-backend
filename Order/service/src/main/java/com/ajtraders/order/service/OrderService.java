package com.ajtraders.order.service;

import com.ajtraders.order.dto.OrderDTO;
import com.ajtraders.order.dto.ResponseDto;
import com.ajtraders.order.entity.Order;
import com.ajtraders.order.exception.ServiceException;
import com.ajtraders.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public ResponseDto addOrder(OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        Order savedOrder = orderRepository.save(order);
        return new ResponseDto("Order created Successfully. Your order Id is "+ savedOrder.getId());
    }

    public List<OrderDTO> getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(order, orderDTO);
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested order is not available", "Not Found", HttpStatus.NOT_FOUND));
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    public ResponseDto deleteById(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested order is not available", "Not Found", HttpStatus.NOT_FOUND));
        orderRepository.deleteById(id);
        return new ResponseDto("The order has been deleted successfully");
    }

    public ResponseDto updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested order is not available", "Not Found", HttpStatus.NOT_FOUND));
        BeanUtils.copyProperties(orderDTO, order);
        orderRepository.save(order);
        return new ResponseDto("The order has been updated successfully");
    }
}
