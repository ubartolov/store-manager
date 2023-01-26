package com.example.storemanager.dto;

import lombok.Data;

@Data
public class RequestProductWarehouseDto {

    private String productName;
    private Integer productPrice;
    private Integer requestAmount;
    private Long warehouseId;
}
