package com.example.storemanager.service;

import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.model.Store;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StoreService {

    List<Store> findAll();


    Store saveOrUpdate(Store store);

    Store addOrUpdateStore (StoreDto storeDto);

    void delete(Store store);

    void deleteById(Long id);

    Store findById(Long id);


    StoreDto findByIdDto(Long id);

    StoreDto copyStoreToStoreDto(Store store);

    List<Store> findAllStores();

    List<Store> findAllWarehouses();
}
