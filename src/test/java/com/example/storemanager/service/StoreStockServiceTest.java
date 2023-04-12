package com.example.storemanager.service;

import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dto.DeleteProductDto;
import com.example.storemanager.dto.RequestProductDto;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;
import com.example.storemanager.service.impl.ProductServiceImpl;
import com.example.storemanager.service.impl.StoreServiceImpl;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StoreStockServiceTest {

    @MockBean
    StoreStockRepository storeStockRepository;

    @MockBean
    StoreService storeService;

    @MockBean
    ProductService productService;

    @Autowired
    private StoreStockService storeStockService;

//    @Test
//    public void deleteProductFromWarehouseStockSuccessful () {
//
//        Store existingStore = TestUtil.createTransientStoreWithId();
//        Product existingProduct = TestUtil.createTransientProductWithId();
//
//        DeleteProductDto deleteProductDto = new DeleteProductDto();
//        deleteProductDto.setProductId(existingProduct.getProductId());
//        deleteProductDto.setStoreId(existingStore.getStoreId());
//
//        when(storeService.findById(deleteProductDto.getStoreId())).thenReturn(existingStore);
//        when(productService.findById(deleteProductDto.getProductId())).thenReturn(existingProduct);
//
//        StoreStock existingStoreStock = new StoreStock();
//        existingStoreStock.setProduct(existingProduct);
//        existingStoreStock.setStore(existingStore);
//        existingStoreStock.setStoreStockId(123L);
//        existingStoreStock.setQuantity(500);
//
//        Optional<StoreStock> optionalStorestock = Optional.of(existingStoreStock);
//
//        when(storeStockRepository.findByStoreAndProduct(existingStore, existingProduct)).thenReturn(optionalStorestock);
//
//        storeStockService.deleteProductFromStoreStock(deleteProductDto);
//    }
//
//    @Test
//    public void addQuantityFromWarehouseToStoreSuccessful() {
//
//        Store existingStore = TestUtil.createTransientStoreWithId();
//        Store existingWarehouse = TestUtil.createTransientWarehouseWithId();
//        Product existingProduct = TestUtil.createTransientProductWithId();
//
//        RequestProductDto requestProductDto = new RequestProductDto();
//        requestProductDto.setWarehouseId(existingWarehouse.getStoreId());
//        requestProductDto.setStoreId(existingStore.getStoreId());
//        requestProductDto.setProductId(existingProduct.getProductId());
//        requestProductDto.setRequestAmount(50);
//
//        when(storeService.findById(requestProductDto.getStoreId())).thenReturn(existingStore);
//        when(productService.findById(requestProductDto.getProductId())).thenReturn(existingProduct);
//        when(storeService.findById(requestProductDto.getWarehouseId())).thenReturn(existingWarehouse);
//
//        StoreStock storeStock = new StoreStock();
//        storeStock.setStore(existingStore);
//        storeStock.setProduct(existingProduct);
//        storeStock.setQuantity(500);
//        storeStock.setStoreStockId(113L);
//
//        StoreStock warehouseStock = new StoreStock();
//        warehouseStock.setStore(existingWarehouse);
//        warehouseStock.setProduct(existingProduct);
//        warehouseStock.setQuantity(500);
//        warehouseStock.setStoreStockId(113L);
//
//        Optional<StoreStock> optionalStoreStock = Optional.of(storeStock);
//        Optional<StoreStock> optionalWarehouseStock = Optional.of(warehouseStock);
//
//        when(storeStockRepository.findByStoreAndProduct(existingStore, existingProduct)).thenReturn(optionalStoreStock);
//        when(storeStockRepository.findByStoreAndProduct(existingWarehouse, existingProduct)).thenReturn(optionalWarehouseStock);
//
//        storeStock.setQuantity(storeStock.getQuantity() + requestProductDto.getRequestAmount());
//        warehouseStock.setQuantity(warehouseStock.getQuantity() - requestProductDto.getRequestAmount());
//
//        when(storeStockRepository.save(storeStock)).thenReturn(storeStock);
//        when(storeStockRepository.save(warehouseStock)).thenReturn(warehouseStock);
//
//        StoreStock storeResult = storeStockService.saveOrUpdate(storeStock);
//        StoreStock warehouseResult = storeStockService.saveOrUpdate(warehouseStock);
//
//        assertEquals(storeStock.getQuantity(), storeResult.getQuantity());
//        assertEquals(warehouseResult.getQuantity(), warehouseResult.getQuantity());
//    }
}
