package com.example.storemanager.dto;

import com.example.storemanager.constants.StoreType;
import lombok.Data;

import java.util.Objects;

@Data
public class StoreStockDto {


   private Long storeId;
   private Long productId;
   private StoreType storeType;
   private String productName;
   private Integer productPrice;
   private Integer quantity;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof StoreStockDto that)) return false;
      return Objects.equals(storeId, that.storeId) && Objects.equals(productId, that.productId) && storeType == that.storeType && Objects.equals(productName, that.productName) && Objects.equals(productPrice, that.productPrice) && Objects.equals(quantity, that.quantity);
   }

   @Override
   public int hashCode() {
      return Objects.hash(storeId, productId, storeType, productName, productPrice, quantity);
   }
}
