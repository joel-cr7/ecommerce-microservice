package com.ecommerce.order_service.dto;


import lombok.Data;

@Data
public class OrderRequestItemsDTO {
    private Long id;
    private long productId;
    private Integer quantity;
}
