package com.example.storemanager.integration;

import com.example.storemanager.dao.ProductRepository;
import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.dto.DeleteProductDto;
import com.example.storemanager.dto.RequestProductDto;
import com.example.storemanager.dto.RequestProductWarehouseDto;
import com.example.storemanager.dto.UpdateProductDto;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.ProductService;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StoreStockServiceIntegrationTest {

    @Autowired
    private StoreStockRepository storeStockRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreStockService storeStockService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @Test
    @Transactional
    public void deleteProductFromWarehouseStockSuccessful() {

        Store existingStore = testUtil.createPersistentWarehouseWithStock();
        assertEquals(existingStore.getStoreStock().size(), 1);
        Product existingProduct = existingStore.getStoreStock().get(0).getProduct();
        Long existingStoreStockId = existingStore.getStoreStock().get(0).getStoreStockId();

        DeleteProductDto deleteProductDto = new DeleteProductDto();
        deleteProductDto.setProductId(existingProduct.getProductId());
        deleteProductDto.setStoreId(existingStore.getStoreId());


        storeStockService.deleteProductFromStoreStock(deleteProductDto);

        Optional<StoreStock> result = storeStockRepository.findById(existingStoreStockId);
        assertFalse(result.isPresent());
        Optional<Store> resultStore = storeRepository.findById(existingStore.getStoreId());
        assertTrue(resultStore.isPresent());
        assertTrue(resultStore.get().getStoreStock().isEmpty());
    }

    @Test
    @Transactional
    public void deleteProductFromWarehouseStockFailed() {
        DeleteProductDto deleteProductDto = new DeleteProductDto();
        deleteProductDto.setProductId(0L);
        deleteProductDto.setStoreId(0L);
        /*when(storeService.findById(any())).thenThrow(new AppException("Store/Warehouse Not Found"));
        when(productService.findById(any())).thenThrow(new AppException("Product Not Found"));*/

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            storeStockService.deleteProductFromStoreStock(deleteProductDto);
        },"Store/Product were not found");

        assertEquals("Store with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void deleteProductFromWarehouseStockNoProductFailed() {
        Store warehouse = testUtil.createPersistentWarehouse();

        DeleteProductDto deleteProductDto = new DeleteProductDto();
        deleteProductDto.setProductId(0L);
        deleteProductDto.setStoreId(warehouse.getStoreId());
        /*when(storeService.findById(any())).thenThrow(new AppException("Store/Warehouse Not Found"));
        when(productService.findById(any())).thenThrow(new AppException("Product Not Found"));*/

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            storeStockService.deleteProductFromStoreStock(deleteProductDto);
        });

        assertEquals("Product with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void addQuantityFromWarehouseToStoreSuccessful() {

        Store existingStore = testUtil.createPersistentStore();
        Store existingWarehouse = testUtil.createPersistentWarehouseWithStock();

        assertTrue(existingStore.getStoreStock().isEmpty());

        StoreStock warehouseStoreStock = existingWarehouse.getStoreStock().get(0);
        Integer originalQuantityInWarehouse = warehouseStoreStock.getQuantity();
        Product productInWarehouse = warehouseStoreStock.getProduct();

        StoreStock storeStock = new StoreStock();
        storeStock.setProduct(productInWarehouse);
        storeStock.setQuantity(0);
        existingStore.addStoreStock(storeStock);

        productInWarehouse.addStoreStock(storeStock);
        productInWarehouse = productRepository.save(productInWarehouse);
        existingStore.getStoreStock().add(storeStock);
        existingStore = storeRepository.save(existingStore);

        assertFalse(existingStore.getStoreStock().isEmpty());


        RequestProductDto requestProductDto = new RequestProductDto();
        requestProductDto.setWarehouseId(existingWarehouse.getStoreId());
        requestProductDto.setStoreId(existingStore.getStoreId());
        requestProductDto.setProductId(productInWarehouse.getProductId());
        requestProductDto.setRequestAmount(50);

        storeStockService.addQuantityFromWarehouse(requestProductDto);

        assertEquals(50, (int) existingStore.getStoreStock().get(0).getQuantity());
        assertEquals((int) existingWarehouse.getStoreStock().get(0).getQuantity(), (originalQuantityInWarehouse - 50));
        Store resultStore = storeService.findById(existingStore.getStoreId());
        Store resultWarehouse = storeService.findById(existingWarehouse.getStoreId());
        assertEquals(50, (int) resultStore.getStoreStock().get(0).getQuantity());
        assertEquals((int) resultWarehouse.getStoreStock().get(0).getQuantity(), (originalQuantityInWarehouse - 50));
    }

    @Test
    @Transactional
    public void returnProductToWarehouseSuccessful() {
        Store existingStore = testUtil.createPersistentStoreWithStock();
        Store existingWarehouse = testUtil.createPersistentWarehouse();
        Product productInStore = existingStore.getStoreStock().get(0).getProduct();

        StoreStock existingStoreStock = existingStore.getStoreStock().get(0);
        Integer originalStoreQuantity = existingStoreStock.getQuantity();

        StoreStock warehouseStock = new StoreStock();
        warehouseStock.setStore(existingWarehouse);
        warehouseStock.setProduct(productInStore);
        warehouseStock.setQuantity(0);
        existingWarehouse.addStoreStock(warehouseStock);

        productInStore.getStoreStock().add(warehouseStock);
        productRepository.save(productInStore);

        existingWarehouse.getStoreStock().add(warehouseStock);
        existingWarehouse = storeRepository.save(existingWarehouse);

        Integer originalWarehouseQuantity = existingWarehouse.getStoreStock().get(0).getQuantity();

        RequestProductDto requestProductDto = new RequestProductDto();
        requestProductDto.setWarehouseId(existingWarehouse.getStoreId());
        requestProductDto.setStoreId(existingStore.getStoreId());
        requestProductDto.setProductId(existingStore.getStoreStock().get(0).getProduct().getProductId());
        requestProductDto.setRequestAmount(50);

        storeStockService.returnProductToWarehouse(requestProductDto);

        assertTrue(existingStore.getStoreStock().get(0).getQuantity() == (originalStoreQuantity - 50));
        assertTrue(existingWarehouse.getStoreStock().get(0).getQuantity() == (originalWarehouseQuantity + 50));
    }

    @Test
    @Transactional
    public void addNewProductToStoreSuccessful() {

        Store existingWarehouse = testUtil.createPersistentWarehouseWithStock();
        Store existingStore = testUtil.createPersistentStore();

        Product productInWarehouse = existingWarehouse.getStoreStock().get(0).getProduct();


        RequestProductDto requestProductDto = new RequestProductDto();
        requestProductDto.setWarehouseId(existingWarehouse.getStoreId());
        requestProductDto.setStoreId(existingStore.getStoreId());
        requestProductDto.setProductId(productInWarehouse.getProductId());
        requestProductDto.setRequestAmount(50);

        storeStockService.addNewProductToStore(requestProductDto);

        Store resultStore = storeService.findById(existingStore.getStoreId());
        StoreStock resultStoreStock = resultStore.getStoreStock().get(0);
        assertEquals(resultStoreStock.getProduct().getProductId(), productInWarehouse.getProductId());
    }

    @Test
    @Transactional
    public void addProductToWarehouseSameProductNameSuccessful() {

        Store existingWarehouse = testUtil.createPersistentWarehouseWithStock();
        Product product = existingWarehouse.getStoreStock().get(0).getProduct();
        Integer originalQuantity = existingWarehouse.getStoreStock().get(0).getQuantity();

        assertEquals(existingWarehouse.getStoreStock().size(), 1);

        RequestProductWarehouseDto requestProductWarehouseDto = new RequestProductWarehouseDto();
        requestProductWarehouseDto.setProductName(product.getProductName());
        requestProductWarehouseDto.setProductPrice(1000);
        requestProductWarehouseDto.setWarehouseId(existingWarehouse.getStoreId());
        requestProductWarehouseDto.setRequestAmount(500);

        storeStockService.addNewProductToWarehouse(requestProductWarehouseDto);

        Store savedWarehouse = storeService.findById(existingWarehouse.getStoreId());


        assertEquals(savedWarehouse.getStoreStock().get(0).getProduct().getProductName(), requestProductWarehouseDto.getProductName());
        assertEquals((int) savedWarehouse.getStoreStock().get(0).getQuantity(), (originalQuantity + requestProductWarehouseDto.getRequestAmount()));
        assertEquals(savedWarehouse.getStoreStock().size(), 1);

    }

    @Test
    @Transactional
    public void addProductToWarehouseDifferentProductNameSuccessful() {

        Store existingWarehouse = testUtil.createPersistentWarehouse();

        RequestProductWarehouseDto requestProductWarehouseDto = new RequestProductWarehouseDto();
        requestProductWarehouseDto.setProductName("Strawberry" + StringUtils.randomAlphanumeric(15));
        requestProductWarehouseDto.setProductPrice(1000);
        requestProductWarehouseDto.setWarehouseId(existingWarehouse.getStoreId());
        requestProductWarehouseDto.setRequestAmount(500);

        storeStockService.addNewProductToWarehouse(requestProductWarehouseDto);

        Store resultWarehouse = storeRepository.findById(existingWarehouse.getStoreId()).get();

        assertEquals(resultWarehouse.getStoreStock().get(0).getProduct().getProductName(), requestProductWarehouseDto.getProductName());

    }

    @Test
    @Transactional
    public void updateProductQuantitySuccessful() {
        Store existingWarehouse = testUtil.createPersistentWarehouseWithStock();
        Product existingProduct = existingWarehouse.getStoreStock().get(0).getProduct();

        Integer originalQuantity = existingWarehouse.getStoreStock().get(0).getQuantity();

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setWarehouseId(existingWarehouse.getStoreId());
        updateProductDto.setRequestAmount(50);
        updateProductDto.setProductId(existingProduct.getProductId());

        storeStockService.updateProductQuantity(updateProductDto);

        assertTrue(existingWarehouse.getStoreStock().get(0).getQuantity()
                == (originalQuantity + updateProductDto.getRequestAmount()));
    }

    @Test
    @Transactional
    public void deleteWarehouseSuccessful() {
        Store existingWarehouse = testUtil.createPersistentWarehouseWithWorkersAndStock();
        Store transferWarehouse = testUtil.createPersistentWarehouseWithWorkersAndStock();

        Worker orignalWorker = existingWarehouse.getWorkerList().get(0);

        storeStockService.deleteWarehouse(existingWarehouse.getStoreId(), transferWarehouse.getStoreId());

        assertSame(transferWarehouse.getWorkerList().get(1), orignalWorker);
        assertFalse(storeRepository.findById(existingWarehouse.getStoreId()).isPresent());
    }
}
