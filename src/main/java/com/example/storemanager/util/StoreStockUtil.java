package com.example.storemanager.util;

import com.example.storemanager.model.Product;
import com.example.storemanager.model.StoreStock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreStockUtil {

    public static StoreStock findStoreStockByProductId(List<StoreStock> storeStockList, Long productId) {

        Map<Long, StoreStock> productsMap = new HashMap<>();

        for (int i = 0; i < storeStockList.size(); i++) {
            StoreStock storeStock = storeStockList.get(i);
            productsMap.put(storeStock.getProduct().getProductId(), storeStock);
        }

        if (productsMap.containsKey(productId)) {
            return productsMap.get(productId);
        }

        return null;
    }

}
