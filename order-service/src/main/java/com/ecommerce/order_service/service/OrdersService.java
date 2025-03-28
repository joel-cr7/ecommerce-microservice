package com.ecommerce.order_service.service;


import com.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.OrderStatus;
import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;
    private final ModelMapper modelMapper;


    public List<OrderRequestDTO> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream()
                .map(o -> modelMapper.map(o, OrderRequestDTO.class))
                .toList();
    }


    public OrderRequestDTO getOrderById(Long orderId) {
        log.info("Getting order with ID: {}", orderId);
        Optional<Orders> order = ordersRepository.findById(orderId);
        return order.map(o -> modelMapper.map(o, OrderRequestDTO.class))
                .orElseThrow(() -> new RuntimeException("Order not found !"));
    }

//    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
//    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDTO createOrder(OrderRequestDTO orderRequestDTO) {
        log.info("Calling the createOrder method");
        Double totalPrice = inventoryOpenFeignClient.reduceStock(orderRequestDTO);
        Orders order = modelMapper.map(orderRequestDTO, Orders.class);
        for(OrderItem orderItem: order.getOrderItems()){
            orderItem.setOrder(order);
        }
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        Orders savedOrder = ordersRepository.save(order);
        return modelMapper.map(savedOrder, OrderRequestDTO.class);
    }

    public OrderRequestDTO createOrderFallback(OrderRequestDTO orderRequestDTO, Throwable throwable) {
        log.error("Fallback occurred due to: {}", throwable.getMessage());
        return new OrderRequestDTO();
    }
}
