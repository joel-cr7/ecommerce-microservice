package com.ecommerce.order_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long id;
    private List<OrderRequestItemsDTO> items;
    private BigDecimal totalPrice;
}
