package com.example.storemanager.service;

import com.example.storemanager.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findByName(String product);

    List<Product> findByProductNotIn(Long storeId);

    Product saveOrUpdate(Product product);

    void delete(Product product);

    Product findById(Long productId);
}
