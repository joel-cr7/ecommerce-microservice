package com.ecommerce.inventory_service.service;


import com.ecommerce.inventory_service.dto.OrderRequestDTO;
import com.ecommerce.inventory_service.dto.OrderRequestItemDTO;
import com.ecommerce.inventory_service.dto.ProductDTO;
import com.ecommerce.inventory_service.entity.Product;
import com.ecommerce.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public List<ProductDTO> getAllProducts() {
        log.info("Fetching all inventory items");
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(post -> modelMapper.map(post, ProductDTO.class))
                .toList();
    }


    public ProductDTO getProductById(Long productId) {
        log.info("Getting product with ID: {}", productId);
        Optional<Product> inventory = productRepository.findById(productId);
        return inventory.map(product -> modelMapper.map(product, ProductDTO.class))
                .orElseThrow(() -> new RuntimeException("Inventory not found !"));
    }

    @Transactional
    public Double reduceStocks(OrderRequestDTO orderRequestDTO) {
        log.info("Reducing the stocks");
        Double totalPrice = 0.0;
        for(OrderRequestItemDTO orderRequestItemDTO: orderRequestDTO.getItems()){
            Long productId = orderRequestItemDTO.getProductId();
            Integer quantity = orderRequestItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found wth ID: "+ productId));

            if(product.getStock() < quantity){
                throw new RuntimeException("Product cannot be fulfilled for given quantity");
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            totalPrice += product.getPrice() * quantity;
        }

        return totalPrice;
    }
}
