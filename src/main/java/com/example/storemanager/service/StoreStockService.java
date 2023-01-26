package com.example.storemanager.service;

import com.example.storemanager.dto.*;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;

import java.util.List;


public interface StoreStockService {

    List<StoreStock> findAll();


    StoreStock saveOrUpdate(StoreStock storeStock);

    void delete(StoreStock storeStock);

    StoreStock findById(Long id);

    void deleteProductFromStoreStock(DeleteProductDto deleteProductDto);

    void returnProductToWarehouse(DeleteProductDto deleteProductDto);

    StoreStock getStockForWarehouseAndProduct(Long warehouseId, Long productId);

    List<RequestProductDto> addNewProductToStoreList(List<RequestProductDto> requestProductDtoList);

    List<RequestProductWarehouseDto> addNewProductToWarehouseList(List<RequestProductWarehouseDto> warehouseDtoList);

    void addQuantityFromWarehouse(RequestProductDto requestProductDto);

    void returnProductToWarehouse(RequestProductDto requestProductDto);

    RequestProductDto addNewProductToStore(RequestProductDto requestProductDto);

    void addNewProductToWarehouse(RequestProductWarehouseDto warehouseDto);

    StoreStock findByStoreAndProduct(Store store, Product product);

    List<StoreStockDto> getDetailsById(Long storeId);

    void updateProductQuantity(UpdateProductDto updateProductDto);
}
