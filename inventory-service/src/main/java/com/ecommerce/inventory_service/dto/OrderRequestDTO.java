package com.ecommerce.inventory_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private List<OrderRequestItemDTO> items;
}
