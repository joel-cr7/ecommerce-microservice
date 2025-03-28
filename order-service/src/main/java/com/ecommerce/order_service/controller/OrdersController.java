package com.ecommerce.order_service.controller;


import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/core")
@RefreshScope
public class OrdersController {

    private final OrdersService ordersService;

    @Value("${my.variable}")
    private String myVariable;

    @GetMapping("/testConfig")
    public String testConfigServer(){
        return "Hello from orders service ! my variable is: "+myVariable;
    }

    @GetMapping("/helloOrders")
    public String helloOrders(@RequestHeader("X-User-Id") Long userId){
        return "Hello from orders service ! User id is: "+userId;
    }


    @GetMapping
    public ResponseEntity<List<OrderRequestDTO>> getAllOrders(){
        log.info("Fetching all orders via controller");
        List<OrderRequestDTO> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDTO> getOrderById(@PathVariable Long id){
        OrderRequestDTO order = ordersService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        OrderRequestDTO orderRequest = ordersService.createOrder(orderRequestDTO);
        return ResponseEntity.ok(orderRequest);
    }
}
