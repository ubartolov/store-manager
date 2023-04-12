package com.example.storemanager.service.impl;

import com.example.storemanager.dao.ProductRepository;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;
import com.example.storemanager.service.ProductService;
import com.example.storemanager.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private StoreService storeService;

    public ProductServiceImpl(ProductRepository productRepository, StoreService storeService) {
        this.productRepository = productRepository;
        this.storeService = storeService;
    }

    @Override
    public List<Product> findAll() {
        List<Product> productsList = new ArrayList<>();
        productRepository.findAll().forEach(productsList::add);
        return productsList;
    }
    @Override
    public Product findByName(String product) {
        Optional<Product> productResult = productRepository.findByProductName(product);
        if (productResult.isPresent())
            return productResult.get();
        throw new AppException("Product with the given name does not exist");
    }


    @Override
    public List<Product> findByProductNotIn(Long storeId) {
        Store store = storeService.findById(storeId);
        List<Long> productIdsAlreadyInStore = new ArrayList<>();
        Optional<List<Product>> optionalProducts;

        for (StoreStock storeStock : store.getStoreStock()) {
            productIdsAlreadyInStore.add(storeStock.getProduct().getProductId());
        }
        if (productIdsAlreadyInStore.size() > 0) {
            optionalProducts = productRepository.findByProductIdNotIn(productIdsAlreadyInStore);
            if (optionalProducts.isPresent()) {
                return optionalProducts.get();
            }
        }
        return findAll();
    }

    @Override
    public Product saveOrUpdate(Product product) {
        if (product.getProductId() != null) {
            Optional<Product> optionalProduct = productRepository.findById(product.getProductId());
            if (optionalProduct.isPresent()) {
                Product existingProduct = optionalProduct.get();
                existingProduct.setProductName(product.getProductName());
                existingProduct.setProductPrice(product.getProductPrice());
                existingProduct.setStoreStock(product.getStoreStock());
                return productRepository.save(existingProduct);
            }
        }
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Product findById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new AppException(String.format("Product with the ID number '%d' cannot be found", productId));
    }
}
