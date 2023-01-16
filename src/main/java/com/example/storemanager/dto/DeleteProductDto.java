package com.example.storemanager.dto;

import lombok.Data;

@Data
public class DeleteProductDto {

    private Long storeId;
    private Long productId;
}
