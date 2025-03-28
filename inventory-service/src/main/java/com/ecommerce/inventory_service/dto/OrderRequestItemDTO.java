package com.ecommerce.inventory_service.dto;


import lombok.Data;

@Data
public class OrderRequestItemDTO {
    private long productId;
    private Integer quantity;
}
