package com.example.storemanager.service;

import com.example.storemanager.model.Store;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StoreService {

    List<Store> findAll();

    Store saveOrUpdate(Store store);

    void delete(Store store);

    Store findById(Long id);

    List<Store> findAllStores();

    List<Store> findAllWarehouses();
}
