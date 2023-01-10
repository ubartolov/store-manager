package com.example.storemanager.dao;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.model.Store;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends CrudRepository<Store, Long> {

    Optional<List<Store>> findByStoreType(StoreType storeType);

}
