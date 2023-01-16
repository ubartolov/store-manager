package com.example.storemanager.dto;

import lombok.Data;

@Data
public class UpdateProductDto {

    private Integer requestAmount;
    private Long warehouseId;
    private Long productId;
}
