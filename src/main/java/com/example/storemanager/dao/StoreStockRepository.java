package com.example.storemanager.dao;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreStockRepository extends CrudRepository<StoreStock, Long> {

    Optional<StoreStock> findByStoreAndProduct(Store store, Product product);



}
