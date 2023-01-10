package com.example.storemanager.dto;

import lombok.Data;

@Data
public class RequestProductDto {

    private Long warehouseId;
    private Long storeId;
    private Long productId;
    private Integer requestAmount;
}
