package com.ecommerce.inventory_service.controller;


import com.ecommerce.inventory_service.clients.OrdersFeignClient;
import com.ecommerce.inventory_service.dto.OrderRequestDTO;
import com.ecommerce.inventory_service.dto.ProductDTO;
import com.ecommerce.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;
    private final OrdersFeignClient ordersFeignClient;

    @GetMapping("/fetchOrders")
    public String fetchFomOrdersService(HttpServletRequest httpServletRequest){
        ServiceInstance orderService = discoveryClient.getInstances("order-service").getFirst();

//        String response = restClient.get()
//                .uri(orderService.getUri() + "/orders/core/helloOrders")
//                .retrieve()
//                .body(String.class);

        return ordersFeignClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id){
        ProductDTO inventory = productService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDTO orderRequestDTO){
        Double totalPrice = productService.reduceStocks(orderRequestDTO);
        return ResponseEntity.ok(totalPrice);
    }
}
