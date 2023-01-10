package com.example.storemanager.dto;

import com.example.storemanager.constants.StoreType;
import lombok.Data;

@Data
public class StoreStockDto {


   private Long storeId;
   private Long productId;
   private StoreType storeType;
   private String productName;
   private Integer productPrice;
   private Integer quantity;

}
