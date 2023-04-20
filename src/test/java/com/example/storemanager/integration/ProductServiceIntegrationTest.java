package com.example.storemanager.integration;

import com.example.storemanager.dao.ProductRepository;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;
import com.example.storemanager.service.ProductService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private StoreStockService storeStockService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    public void findByIdSuccessful() {
        Product product = testUtil.createPersistentProduct();

        Optional<Product> result = productRepository.findById(product.getProductId());

        assertTrue(result.isPresent());
    }
    @Test
    @Transactional
    public void findByIdFailed () {

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            productService.findById(0L);
        });
        assertEquals("Product with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void findByNameSuccessful() {

        Product product = testUtil.createPersistentProduct();

        Optional<Product> result = productRepository.findByProductName(product.getProductName());

        assertTrue(result.isPresent());
    }

    @Test
    @Transactional
    public void findByProductNotInSuccessful() {
        Store store = testUtil.createPersistentStoreWithStock();

        List<Product> result = productService.findByProductNotIn(store.getStoreId());

        assertFalse(result.contains(store.getStoreStock().get(0).getProduct()));
    }


}
