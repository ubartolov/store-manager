package com.example.storemanager.service;

import com.example.storemanager.dto.RequestProductDto;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;

import java.util.List;


public interface StoreStockService {

    List<StoreStock> findAll();


    StoreStock saveOrUpdate(StoreStock storeStock);

    void delete(StoreStock storeStock);

    StoreStock findById(Long id);

    StoreStock getStockForWarehouseAndProduct(Long warehouseId, Long productId);

    void addNewProductToStoreList(List<RequestProductDto> requestProductDtoList);

    void addAndSubtractQuantity(RequestProductDto requestProductDto);

    void addNewProductToStore(RequestProductDto requestProductDto);

    StoreStock findByStoreAndProduct(Store store, Product product);
}
